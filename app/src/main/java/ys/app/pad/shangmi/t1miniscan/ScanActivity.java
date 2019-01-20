package ys.app.pad.shangmi.t1miniscan;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sunmi.scan.Config;
import com.sunmi.scan.Image;
import com.sunmi.scan.ImageScanner;
import com.sunmi.scan.Symbol;
import com.sunmi.scan.SymbolSet;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.shangmi.t1miniscan.utils.BitmapTransformUtils;
import ys.app.pad.shangmi.t1miniscan.utils.EaiUtil;
import ys.app.pad.shangmi.t1miniscan.utils.LogUtil;
import ys.app.pad.shangmi.t1miniscan.utils.SoundUtils;


public class ScanActivity extends BaseActivity implements SurfaceHolder.Callback {
    private static final String TAG = "ScanActivity";
    private Camera mCamera;
    private SurfaceHolder mHolder;
    private SurfaceView mSurfaceView;
    private ImageScanner mImageScanner;//声明扫描器
    private Handler mAutoFocusHandler;
    private AsyncDecode asyncDecode;
    private SoundUtils soundUtils;
    private AtomicBoolean isRUN = new AtomicBoolean(false);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t1mini);
        setBackVisiable();
        setTitle("请扫描");
        setBgWhiteStatusBar();
        setConfig();
        init();
    }

    private void setConfig() {
        Log.i(TAG, "setConfig: ");
        Intent intent = getIntent();

        LogUtil.e(TAG, "intent.getExtras() != nul");
        //当前分辨率
            ScanConfig.CURRENT_PPI = 0X0003;
        //扫描完成声音提示
        ScanConfig.PLAY_SOUND = true;
        //扫描完成震动
        ScanConfig.PLAY_VIBRATE = false;
        //识别反色二维码
        ScanConfig.IDENTIFY_INVERSE_QR_CODE = true;
        //识别画面中多个二维码
        ScanConfig.IDENTIFY_MORE_CODE = false;
        //是否显示设置按钮
//            ScanConfig.IS_SHOW_SETTING = intent.getBooleanExtra("IS_SHOW_SETTING", true);
        //是否显示选择相册按钮
//            ScanConfig.IS_SHOW_ALBUM = intent.getBooleanExtra("IS_SHOW_ALBUM", true);
        //是否显示闪光灯
        ScanConfig.IS_OPEN_LIGHT = true;
        //是否是循环模式
        ScanConfig.SCAN_MODE = false;

    }

    private void init() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);
        mAutoFocusHandler = new Handler();
        mHolder = mSurfaceView.getHolder();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.addCallback(this);
        mImageScanner = new ImageScanner();//创建扫描器
        mImageScanner.setConfig(0, Config.X_DENSITY, 2);//行扫描间隔
        mImageScanner.setConfig(0, Config.Y_DENSITY, 2);//列扫描间隔
        final FrameLayout view= (FrameLayout) findViewById(R.id.lay_root);
        final ImageView imageView= (ImageView) findViewById(R.id.iv_scan_needle);

        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", view.getHeight());
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initBeepSound();
        initScanConfig();
        startFlash();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void startFlash() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ScanConfig.IS_OPEN_LIGHT) {
                    EaiUtil.controlLamp("1", ScanConfig.LIGHT_BRIGHT_TIME, ScanConfig.LIGHT_DROWN_TIME, EaiUtil.LED_CAM_NAME);
                } else {
                    EaiUtil.controlLamp("0", ScanConfig.LIGHT_BRIGHT_TIME, ScanConfig.LIGHT_DROWN_TIME, EaiUtil.LED_CAM_NAME);
                }
            }
        }).start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        EaiUtil.controlLamp("0", ScanConfig.LIGHT_BRIGHT_TIME, ScanConfig.LIGHT_DROWN_TIME, EaiUtil.LED_CAM_NAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAutoFocusHandler != null) {
            mAutoFocusHandler.removeCallbacksAndMessages(null);
            mAutoFocusHandler = null;
        }
    }

    private void initScanConfig() {
        //是否开启同一幅图一次解多个条码,0表示只解一个，1为多个
        if (ScanConfig.IDENTIFY_MORE_CODE) {
            mImageScanner.setConfig(0, Config.ENABLE_MULTILESYMS, 1);
        } else {
            mImageScanner.setConfig(0, Config.ENABLE_MULTILESYMS, 0);
        }
        //是否开启识别反色二维码,0表示不识别，1为识别
        if (ScanConfig.IDENTIFY_INVERSE_QR_CODE) {
            mImageScanner.setConfig(0, Config.ENABLE_INVERSE, 1);
        } else {
            mImageScanner.setConfig(0, Config.ENABLE_INVERSE, 0);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceCreated");
//        showToast("surfaceCreated");
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            LogUtil.e(TAG, "Camera.open()");
//            showToast("Camera.open()");
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.e(TAG, "surfaceChanged");
//        showToast("surfaceChanged");
        if (mHolder.getSurface() == null) {
            return;
        }
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
        try {
            setCameraParameters();
//            mCamera.setDisplayOrientation(90);//竖屏显示
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(previewCallback);
            mCamera.startPreview();
            mCamera.autoFocus(autoFocusCallback);
        } catch (Exception e) {
            LogUtil.e("DBG", "Error starting camera preview: " + e.getMessage());
//            showToast("DBG"+"Error starting camera preview: ");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtil.e(TAG, "surfaceDestroyed");
//        showToast("surfaceDestroyed");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }


    public void back(View view) {
        finish();
    }

    // 获取最佳的屏幕显示尺寸
    private Point getBestCameraResolution(Camera.Parameters parameters, Point screenResolution) {
        float tmp = 0f;
        float mindiff = 100f;
        float x_d_y = (float) screenResolution.x / (float) screenResolution.y;
        Size best = null;
        List<Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        for (Size s : supportedPreviewSizes) {
            tmp = Math.abs(((float) s.height / (float) s.width) - x_d_y);
            if (tmp < mindiff) {
                mindiff = tmp;
                best = s;
            }
        }
        return new Point(best.width, best.height);
    }

    private void setCameraParameters() {
        if (mCamera == null) return;
        LogUtil.e(TAG, "ScanConfig.CURRENT_PPI=" + ScanConfig.CURRENT_PPI);
        //摄像头预览分辨率设置和图像放大参数设置，非必须，根据实际解码效果可取舍
        Camera.Parameters parameters = mCamera.getParameters();

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        ScanConfig.BEST_RESOLUTION = getBestCameraResolution(parameters, new Point(display.getWidth(), display.getHeight()));

        parameters.setPreviewSize(320, 240);
//        parameters.set("orientation", "portrait");
//        parameters.set("zoom", String.valueOf(27 / 10.0));//放大图像2.7倍
        mCamera.setParameters(parameters);
    }

    /**
     * 预览数据
     */
    PreviewCallback previewCallback = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
//            if (isRUN.compareAndSet(false, true)) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();//获取预览分辨率

            //创建解码图像，并转换为原始灰度数据，注意图片是被旋转了90度的
            Image source = new Image(size.width, size.height, "Y800");
            //图片旋转了90度，将扫描框的TOP作为left裁剪
            source.setData(data);//填充数据
            asyncDecode = new AsyncDecode();
            asyncDecode.execute(source);//调用异步执行解码
//            }
        }
    };


    private class AsyncDecode extends AsyncTask<Image, Void, ArrayList<HashMap<String, String>>> {

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(Image... params) {

            final ArrayList<HashMap<String, String>> result = new ArrayList<>();
            Image src_data = params[0];//获取灰度数据
            //解码，返回值为0代表失败，>0表示成功
            final int data = mImageScanner.scanImage(src_data);
            if (data != 0) {
                playBeepSoundAndVibrate();//解码成功播放提示音
                SymbolSet syms = mImageScanner.getResults();//获取解码结果
                for (Symbol sym : syms) {
                    HashMap<String, String> temp = new HashMap<>();
                    temp.put(ScanConfig.TYPE, sym.getSymbolName());
                    temp.put(ScanConfig.VALUE, sym.getResult());
                    result.add(temp);
                    break;
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<HashMap<String, String>> result) {
            super.onPostExecute(result);

            if (!result.isEmpty()) {
                LogUtil.e(TAG, "!result.isEmpty()");

                Intent intent = new Intent();
                intent.putExtra(Constants.scan_result, result.get(0).get(ScanConfig.VALUE));
                setResult(Constants.scan_result_code, intent);
                finish();

            } else {
                isRUN.set(false);
            }
        }

    }

    /**
     * 自动对焦回调
     */
    AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            if (mAutoFocusHandler != null) {
                mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
            }
        }
    };

    //自动对焦
    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (null == mCamera || null == autoFocusCallback) {
                return;
            }
            mCamera.autoFocus(autoFocusCallback);
        }
    };

    private void initBeepSound() {
        if (soundUtils == null) {
            soundUtils = new SoundUtils(this, SoundUtils.RING_SOUND);
            soundUtils.putSound(0, R.raw.beep);
        }
    }


    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
     /*private final OnCompletionListener beepListener = new OnCompletionListener()
     {
 		public void onCompletion(MediaPlayer mediaPlayer) 
 		{
 			mediaPlayer.seekTo(0);
 		}
 	};*/

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (soundUtils != null && ScanConfig.PLAY_SOUND) {
            soundUtils.playSound(0, SoundUtils.SINGLE_PLAY);
        }
        if (ScanConfig.PLAY_VIBRATE) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

}
