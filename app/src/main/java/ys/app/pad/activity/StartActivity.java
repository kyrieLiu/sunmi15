package ys.app.pad.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;

import ys.app.pad.BaseActivity;
import ys.app.pad.MainActivity;
import ys.app.pad.R;
import ys.app.pad.activity.vice.ViceActivity;
import ys.app.pad.databinding.ActivityStartBinding;
import ys.app.pad.utils.SpUtil;

/**
 * Created by lyy on 2017/2/20 10:05.
 * email：2898049851@qq.com
 */

public class StartActivity extends BaseActivity {

    private ActivityStartBinding binding;
    public static final boolean isMain = Build.MODEL.equals("t1host") || Build.MODEL.equals("T1-G")||Build.MODEL.equals("D1");
    public static final boolean isVice = Build.MODEL.equals("t1sub14");
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private final int permissionRequestCode = 0x1;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_start);
        ImmersionBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
        /**
         * 版本判断，大于23的时候才需要动态申请权限
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.MODEL.equals("t1host")) {
            /**
             * 判断该权限是否已经获取
             */
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            /**
             * PERMISSION_GRANTED  已经获取  PERMISSION_DENIED 拒绝
             */
            if (i != PackageManager.PERMISSION_GRANTED) {
                /**
                 * 如果为获取权限，弹框提示用户当前应用需要该权限的意图
                 */
                showDialogTipUserRequestPermission();
            } else {
                transfer();
            }
        }else{
            transfer();
        }

    }
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//    };
    private void transfer() {
        Log.i("info","机器型号=="+Build.MODEL);
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                Intent intent;
                if (isVice) {
                    intent = new Intent(StartActivity.this, ViceActivity.class);
                    startActivity(intent);
                } else {
                    if (SpUtil.getIsLogin()) {
                        startActivity(new Intent(StartActivity.this, MainActivity.class));
                    } else {
                        startActivity(new Intent(StartActivity.this, LoginActivity.class));
                    }
                }

                finish();
//            }
//        }, 300);

    }


    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {

        new AlertDialog.Builder(this)
                .setTitle(R.string.permission_dialog_tip)
                .setMessage(R.string.permission_dialog_message)
                .setPositiveButton(R.string.granted, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.denied, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).setCancelable(false).show();
    }
    /**
     * 开始请求权限
     */
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, permissionRequestCode);
    }
    /**
     * 用户权限申请回调方法
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == permissionRequestCode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    /**
                     * 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                     */
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } else {
                        finish();
                    }
                } else {
                    Toast.makeText(this, R.string.acquire_permission_success, Toast.LENGTH_SHORT).show();
                    transfer();
                }
            }
        }
    }
    // 提示用户去应用设置界面手动开启权限

    private void showDialogTipUserGoToAppSettting() {
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.permission_denied)
                .setMessage(R.string.goto_setting)
                .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 123);
    }
}
