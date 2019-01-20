package ys.app.pad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpTaskHandler;
import ys.app.pad.utils.Logger;
import ys.app.pad.widget.dialog.LoadingDialog;

public abstract class BaseActivity extends AppCompatActivity implements HttpCycleContext {

    protected AppApplication appApplication;
    protected String TAG;
    //在BaseActivity或BaseFragment中添加字段
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    private LoadingDialog mRDialog;
    public int startHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        clearTask();
        AppManager.getAppManager().addActivity(this);
        //ImmersionBar.with(this).init();
        Logger.i("currentPage -> " + TAG);
    }

    private void clearTask() {
        if ("BaseWidgetActivity".equals(getClass().getSuperclass().getSimpleName())) {
            AppManager.getAppManager().finishAllActivity();
        }
    }

    protected void setBackVisiable() {
        try {
            ImageView backIv = (ImageView) findViewById(R.id.back_iv);
            backIv.setVisibility(View.VISIBLE);

            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        } catch (Exception e) {
            Logger.i(TAG + ": setBackVisiable ->" + e.getMessage());
        }
    }

    protected void setBackVisiable(boolean isSelect) {
        try {
            ImageView backIv = (ImageView) findViewById(R.id.back_iv);
            backIv.setVisibility(View.VISIBLE);
            backIv.setSelected(isSelect);

            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        } catch (Exception e) {
            Logger.i(TAG + ": setBackVisiable ->" + e.getMessage());
        }
    }

    protected void setTitle(String title) {
        try {
            TextView titleTv = (TextView) findViewById(R.id.title_tv);
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(title);

        } catch (Exception e) {
            Logger.i(TAG + ": setTitle ->" + e.getMessage());
        }
    }

    public void turnToActivity(Class<? extends BaseActivity> openActivity) {
        Intent intent = new Intent();
        intent.setClass(this, openActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }

    public void turnToActivityByFinish(Class<? extends BaseActivity> openActivity) {
        Intent intent = new Intent();
        intent.setClass(this, openActivity);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
        finish();
    }


    public void showToast(CharSequence text) {
        Toast.makeText(AppApplication.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showRDialog() {
        if (mRDialog == null) {
            //mRDialog = new RequestDialog(this);
            mRDialog = new LoadingDialog(this);
        }
        if (!isFinishing()&&!mRDialog.isShowing()) {
            mRDialog.show();
        }

    }

    public void hideRDialog() {
        if (mRDialog != null) {
            if (!isFinishing()&&mRDialog.isShowing()) {
                mRDialog.dismiss();
            }
        }
        mRDialog=null;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_from_right);
    }


    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }


    /**
     * 状态栏是白色
     */
    @SuppressLint("ResourceAsColor")
    public void setBgWhiteStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarAlpha(0)
                .init();

        if (ImmersionBar.isSupportStatusBarDarkFont()) {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true)
                    .init();
        } else {
            ImmersionBar.with(this)
                    .statusBarColorInt(R.color.color_main)
                    .init();
        }
    }

    /**
     * 状态栏是其他颜色
     */
    public void setBgOtherStatusBar() {
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarAlpha(0)
                .init();

    }

    @Override
    protected void onDestroy() {
        //ImmersionBar.with(this).destroy();
        //在BaseActivity和BaseFragment销毁方法中添加
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
        AppManager.getAppManager().finishActivity(this);
        if (mRDialog != null&&mRDialog.isShowing()) {
            mRDialog.dismiss();
        }
        super.onDestroy();
    }

}
