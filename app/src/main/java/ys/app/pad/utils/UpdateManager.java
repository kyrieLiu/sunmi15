package ys.app.pad.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseDataResult;
import ys.app.pad.model.UpdateBean;
import ys.app.pad.widget.dialog.UpdateDialog;


/**
 * Created by Administrator on 2017/12/21/021.
 */

public class UpdateManager {
    public static final String TAG = "UpdateManager";
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    /*
     * 服务器 url
     */
    private static final String VERSION_PATH = "http://192.168.11.140:8088/version/version.html";
    /**
     * 更新状态 1：更新中，2：
     */
    private static final int DOWNLOAD_ING = 1;
    private static final int DOWNLOAD_OVER = 2;
    /**
     * 版本号
     */
    private String version_code;
    /**
     * 版本名称
     */
    private String version_name;
    /**
     * 版本描述
     */
    private String version_desc;
    /**
     * 版本路径
     */
    private String version_path;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 保存路径
     */
    private String mSavePath;
    /**
     * 进度条
     */
    private ProgressBar mProgress;
    /**
     * 进度百分比
     */
    private TextView mPrecentTV;
    /**
     * 取消更新按钮
     */
    private TextView tv_cancel;
    /**
     * 后台更新
     */
    private TextView tv_hide;
    /**
     * 进度
     */
    private int progress;
    /**
     * 下载位置
     */
    private int downLength = 0;
    /**
     * 弹框
     */
    private Dialog downDialog;


    public Boolean isCancle() {
        return isCancle;
    }

    public void setCancle(Boolean cancle) {
        isCancle = cancle;
    }

    /**
     * 是否取消
     */
    private Boolean isCancle = false;

    private View view;
    private UpdateBean updateBean;

    public UpdateManager(Context context) {
        mContext = context;
    }


    private Handler progressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    version_code = updateBean.getVersionCode();
                    version_name = updateBean.getVersionNo();
                    version_desc = "hhhhhhhh";
                    version_path = Constants.base_url + updateBean.getVersionResourceUrl();
                    if (isUpdate()) {
                        Log.i("UpdateManager","需要更新");
                        // 显示提示更新对话框
                        showNoticeDialog();
                    } else {
                        Log.i("UpdateManager","已是最新版本");
                    }
                    break;
                case DOWNLOAD_ING:
                    mProgress.setProgress(progress);
                    if (null != mPrecentTV) {
                        mPrecentTV.setText(progress + "%");
                        Log.i("WCJ:DownLoad:", progress + "%");
                    }
                    break;
                case DOWNLOAD_OVER:
                    downDialog.dismiss();
                    installAPK();
                    break;
                case 0x22:
                    Toast.makeText(mContext,"网络连接失败，请检查网络后进行下载",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    /**
     * 检测软件是否需要更新
     */
    public void checkUpdate() {

        //showNoticeDialog();

        try {
            String versionName = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
            Map<String, String> params = new HashMap();
            params.put("version", versionName);
            ApiClient<BaseDataResult<UpdateBean>> apiClient = new ApiClient<>();
            apiClient.reqApi("checkVersion", params, ApiRequest.RespDataType.RESPONSE_JSON)
                    .updateUI(new Callback<BaseDataResult<UpdateBean>>() {
                        @Override
                        public void onSuccess(BaseDataResult<UpdateBean> result) {
                            if (result.isSuccess()) {
                                updateBean = result.getData();
                                progressHandler.sendEmptyMessage(0);
                            } else {
                                Log.e("result.success:false", result.toString());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            Log.e("request:checkVersion", e.toString());
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 是否需要更新
     *
     * @return
     */
    protected boolean isUpdate() {

        int serverVersion = Integer.parseInt(version_code);
        int localVersion = 1;

        try {
            localVersion = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (serverVersion > localVersion) {
            return true;
        }
        return false;
    }


    /*
    * 有更新时显示提示对话框
    */
    protected void showNoticeDialog() {
        final UpdateDialog dialog = new UpdateDialog((Activity) mContext);
        dialog.setCancelVisiable("下次再说", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOkVisiable("立即更新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int permission = ActivityCompat.checkSelfPermission(mContext,
                            "android.permission.WRITE_EXTERNAL_STORAGE");
                    if (permission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                    } else {
                        //下载文件
                        showDownloadDialog();
                    }
                } else {
                    //下载文件
                    showDownloadDialog();
                }
                //取消对话框
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    /*
        * 显示正在下载对话框
        */
    public void showDownloadDialog() {
        view = LayoutInflater.from(mContext).inflate(R.layout.down_progress, null);
        mProgress = (ProgressBar) view.findViewById(R.id.id_Progress);
        mPrecentTV = (TextView) view.findViewById(R.id.tv_progress);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancle = true;
                downDialog.dismiss();
            }
        });
        tv_hide = (TextView) view.findViewById(R.id.tv_hide);
        tv_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downDialog.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setView(view);
        downDialog = builder.create();
        downDialog.show();
        downDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                    isCancle = true;
                    downDialog.dismiss();
                }
                return false;
            }
        });
        downloadAPK();
    }

    /*
    * 开启新线程下载文件
    */
    protected void downloadAPK() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        String sdPath = Environment.getExternalStorageDirectory() + "/";
                        mSavePath = sdPath + "deanDownload";
                        File file = new File(mSavePath);
                        if (!file.exists()) {
                            file.mkdir();
                        }

                        URL url = new URL(version_path);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setConnectTimeout(60000);
                        connection.setReadTimeout(60000);
                        connection.connect();
                        InputStream is = connection.getInputStream();

                        int len = connection.getContentLength();
                        File apkFile = new File(mSavePath, version_name + ".apk");
                        FileOutputStream fos = new FileOutputStream(apkFile);
                        Log.i("apkfile:", is.toString());
                        int count = 0;
                        byte[] buffer = new byte[1024];
                        while (!isCancle) {
                            int numread = is.read(buffer);

                            count += numread;
                            // 计算进度条的当前位置
                            progress = (int) (((float) count / len) * 100);
                            // 更新进度条
                            progressHandler.sendEmptyMessage(DOWNLOAD_ING);

                            // 下载完成
                            if (numread < 0) {
                                progressHandler.sendEmptyMessage(DOWNLOAD_OVER);
                                //SpUtil.setApkPath(apkFile.getPath());
                                Log.i("apkFile:", apkFile.getPath());
                                fos.close();
                                break;
                            }
                            fos.write(buffer, 0, numread);
                        }
                    } else {
                        Log.e("设备问题：", "00000000000000000000000000000,没有安装SD卡");
                    }

                } catch (SocketException e){
                    progressHandler.sendEmptyMessage(0x22);
                }catch (Exception e) {
                    Log.e("DownLoadException:", e.toString());
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /*
     * 下载到本地后执行安装
     */
    protected void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = null;

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdPath = Environment.getExternalStorageDirectory() + "/";
            mSavePath = sdPath + "deanDownload";
            File apkFile = new File(mSavePath, version_name + ".apk");
            if (!apkFile.exists()) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = FileProvider.getUriForFile(mContext,
                        "ys.app.pad.fileprovider",
                        apkFile);
            } else {
                uri = Uri.parse("file://" + apkFile.toString());
            }
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            mContext.startActivity(intent);
        }

    }

}

