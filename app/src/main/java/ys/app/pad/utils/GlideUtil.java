package ys.app.pad.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

import ys.app.pad.AppApplication;
import ys.app.pad.R;

/**
 * Glide加载图片
 * @auther LYY
 * created at 2016/7/29 10:17
 */
public class GlideUtil {

    private volatile static GlideUtil instance = null;
    private GlideUtil(){}

    public static GlideUtil getInstance() {
        if (instance == null) {
            synchronized (GlideUtil.class) {
                if (instance == null) {
                    instance = new GlideUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 普通加载图片
     * @param url
     * @param loading
     * @param imageView
     * @param context
     */
    public void loadImageView(String url , int loading , final ImageView imageView , final Context context){
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.load_img_normal)
                .error(loading)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//缓存原始分辨率图片
//                .animate(R.anim.glide_image_load)//图片载入动画(有小变大)
                .dontAnimate()
                .into(imageView);

    }

    /**
     * 加载圆角图形
     * @param url
     * @param loading
     * @param imageView
     * @param context
     */
    public void loadCircleImageView(String url , int loading , final ImageView imageView , final Context context){
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.mipmap.load_img_circle_normal)
                .error(R.mipmap.load_img_circle_normal)
                .diskCacheStrategy(DiskCacheStrategy.NONE )
                .skipMemoryCache( true )
//                .animate(R.anim.glide_image_load)
                .dontAnimate()
                .into(imageView);

    }


    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {

                AppApplication.getInstance().getExecutorService().execute(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(AppApplication.getInstance()).clearDiskCache();
                    }
                });
            } else {
                Glide.get(AppApplication.getInstance()).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(AppApplication.getInstance()).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache() {
        clearImageDiskCache();
        clearImageMemoryCache();
        deleteFolderFile(getCachePath(), true);
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public Long getCacheSize() {
        try {
            return getFolderSize(new File(AppApplication.getInstance().getCacheDir() + "cache"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    public void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到缓存路径
     * @return
     */
    public String getCachePath(){
        String imgPath= "";
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)
                && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            imgPath  = Environment.getExternalStorageDirectory().getPath() + "ebrun/cache/pics/";
        }else{
            imgPath = AppApplication.getInstance().getDir("cache", Context.MODE_APPEND).getPath();
        }
        return  imgPath;
    }

}
