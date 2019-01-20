package ys.app.pad.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ys.app.pad.shangmi.screen.utils.MainConnectUtils;

/**
 * Created by liuyin on 2018/3/8.
 * 利用Glide下载图片
 */

public class FileHelper {

    private Activity activity;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    private String payMethod,totalMoney;
    private static FileHelper helper;
    private MainConnectUtils utils;

    public static FileHelper getInstance(){
        if (helper==null){
            helper=new FileHelper();
        }
        return helper;
    }
    private FileHelper() {
    }


    public FileHelper setContext(Activity activity){
        this.activity = activity;
        return helper;
    }

    //Glide保存图片
    public void savePicture(final String fileName, final String url,String payMethod,String totalMoney){
        this.payMethod=payMethod;
        this.totalMoney= totalMoney;
        Glide.with(activity).load(url).asBitmap().toBytes().into(new SimpleTarget<byte[]>() {
            @Override
            public void onResourceReady(final byte[] bytes, GlideAnimation<? super byte[]> glideAnimation) {
                int permission = ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                            REQUEST_EXTERNAL_STORAGE);
                }
                saveFileToSD(fileName,bytes);
            }
        });
    }
    //往SD卡写入文件的方法
    public void saveFileToSD(String name, byte[] bytes)  {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file=null;
        String path=null;
        try {
            String rootPath = Environment.getExternalStorageDirectory().getPath();
             path=rootPath + "/"  +name+".png";
             file = new File(path);

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (file!=null){
            utils = MainConnectUtils.getInstance().setContext(activity);
            utils.sendPayMessage(payMethod,totalMoney,path);
        }
    }
    //删除文件夹和文件夹里面的文件
    public static void deleteDir(String path ) {
        File dir = new File(path);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDir(file.getPath()); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }
    public void onPause(){
        if (utils!=null){
            utils.onPause();
        }
    }

}
