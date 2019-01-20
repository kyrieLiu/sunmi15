package ys.app.pad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpTaskHandler;
import ys.app.pad.utils.Logger;
import ys.app.pad.widget.dialog.RequestDialog;

/**
 * Created by lyy on 2016/9/6 14:18.
 * email：2898049851@qq.com
 */
public abstract class BaseFragment extends Fragment implements HttpCycleContext {

    protected AppApplication appApplication;
    protected Activity activity;
    protected int startHttp;
    private String TAG;
    //在BaseActivity或BaseFragment中添加字段
    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;
    private RequestDialog mRDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        TAG = getClass().getSimpleName();
        Logger.i("currentPage -> "+TAG);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
        onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        Logger.d(getClass().getName() + "::setUserVisibleHint(" + isVisibleToUser + ")");
        if (isVisibleToUser) {
            //可见时执行的操作
            isVisible = true;
            lazyLoad();
        } else {
            //不可见时执行的操作
            isVisible = false;
        }
    }

    /**
     * restore the fragment
     *
     * @param saveInstanceState
     */
    public void onRestoreInstanceState(Bundle saveInstanceState) {

    }

    /**
     * Lazy load data for the fragment.
     */
    private void lazyLoad() {

        if (!isPrepared || !isVisible || !isFirst)
        {
            return;
        }
        initData();
        isFirst = false;
    }

    public abstract void initData();


    public void showToast(CharSequence text) {
        Toast.makeText(AppApplication.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }


    protected RelativeLayout bar;

    protected void setBar(RelativeLayout barRl) {
        this.bar = barRl;
    }

    protected void setBarTitle(String title) {
        if (bar == null)
            throw new RuntimeException("you must init bar first");

        TextView titleTv = (TextView) bar.findViewById(R.id.title_tv);
        if (titleTv == null)
            return;
        titleTv.setText(title);
    }

    protected void setBackVisiable(int visiable) {
        if (bar == null)
            throw new RuntimeException("you must init bar first");

        ImageView backIv = (ImageView) bar.findViewById(R.id.back_iv);
        if (backIv == null) return;
        backIv.setVisibility(visiable);
    }


    public void showRDialog() {
        if(mRDialog == null){
            mRDialog = new RequestDialog(activity);
        }
        mRDialog.show();
    }
    public void hideRDialog() {
        if(mRDialog != null){
            mRDialog.hide();
        }
    }

    /**
     * @param @param openActivity 设定文件
     * @return void 返回类型
     * @throws
     * @Title: turnToActivity
     * @Description: Activity跳转不关闭当前Activity操作
     */
    protected void turnToActivity(Class<? extends BaseActivity> openActivity) {
        Intent intent = new Intent(activity, openActivity);
        startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
    }

    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    @Override
    public void onDestroy() {
        if(mRDialog!=null){
            mRDialog.dismiss();
        }
        //在BaseActivity和BaseFragment销毁方法中添加
        HttpTaskHandler.getInstance().removeTask(HTTP_TASK_KEY);
        super.onDestroy();
    }
}
