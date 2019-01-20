package ys.app.pad.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ys.app.pad.R;


/**
 * Created by lyy on 2017/2/28 15:08.
 * email：2898049851@qq.com
 */

public class UpdateDialog extends Dialog {

    private  Activity mActivity;
    private TextView mContentTv;
    private TextView mCancelTv;
    private TextView mOkTv;
    private TextView mSimpleOkTv;
    private TextView mUpdateTv;
    private LinearLayout mDownLl;

    public UpdateDialog(Activity activity) {
        super(activity, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.update_dialog);
        this.mActivity = activity;
        init();
    }

    private void init() {
        mContentTv = (TextView)findViewById(R.id.content_tv);
        mCancelTv = (TextView)findViewById(R.id.cancel_tv);
        mOkTv = (TextView)findViewById(R.id.ok_tv);
        mSimpleOkTv = (TextView)findViewById(R.id.simple_ok_tv);
        mUpdateTv = (TextView)findViewById(R.id.update_tv);
        mDownLl = (LinearLayout)findViewById(R.id.down_ll);
    }

    //设置取消是否可见
    public void setCancelVisiable() {
        mCancelTv.setVisibility(View.VISIBLE);
        mDownLl.setVisibility(View.VISIBLE);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //设置取消是否可见
    public void setCancelVisiable(View.OnClickListener listener) {
        mCancelTv.setVisibility(View.VISIBLE);
        mDownLl.setVisibility(View.VISIBLE);
        mCancelTv.setOnClickListener(listener);
    }

    //设置取消是否可见
    public void setCancelVisiable(String cancelStr,View.OnClickListener listener) {
        mCancelTv.setVisibility(View.VISIBLE);
        mDownLl.setVisibility(View.VISIBLE);
        mCancelTv.setText(cancelStr);
        mCancelTv.setOnClickListener(listener);
    }

    //设置内容
    public void setContent(String content) {
        mContentTv.setVisibility(View.VISIBLE);
        mContentTv.setText(content);
        mContentTv.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    //设置确定是否可见
    public void setOkVisiable(View.OnClickListener listener) {
        mOkTv.setVisibility(View.VISIBLE);
        mDownLl.setVisibility(View.VISIBLE);
        mOkTv.setOnClickListener(listener);
    }

    //设置确定是否可见
    public void setOkVisiable(String okStr,View.OnClickListener listener) {
        mOkTv.setVisibility(View.VISIBLE);
        mDownLl.setVisibility(View.VISIBLE);
        mOkTv.setText(okStr);
        mOkTv.setOnClickListener(listener);
    }

    //设置单按钮确定是否可见
    public void setSimpleOkVisiable() {
        mSimpleOkTv.setVisibility(View.VISIBLE);
        mSimpleOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //设置单按钮确定是否可见
    public void setSimpleOkVisiable(View.OnClickListener listener) {
        mSimpleOkTv.setVisibility(View.VISIBLE);
        mSimpleOkTv.setOnClickListener(listener);
    }

    //设置头标题是否可见
    public void setUpdateTextVisible(String text,boolean isVisable){
        mUpdateTv.setText(text);
        if (isVisable){
            mUpdateTv.setVisibility(View.VISIBLE);
        }else {
            mUpdateTv.setVisibility(View.GONE);
        }
    }

}
