package ys.app.pad.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ys.app.pad.R;

/**
 * Created by lyy on 2017/2/28 15:08.
 * email：2898049851@qq.com
 */

public class CustomDialog extends Dialog {

    private TextView mContentTv;
    private TextView mCancelTv;
    private TextView mOkTv;
    private TextView mSimpleOkTv;
    private LinearLayout mDownLl;

    public CustomDialog(Context activity) {
        super(activity, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.dialog);
        init();
    }

    private void init() {
        mContentTv = (TextView)findViewById(R.id.content_tv);
        mCancelTv = (TextView)findViewById(R.id.cancel_tv);
        mOkTv = (TextView)findViewById(R.id.ok_tv);
        mSimpleOkTv = (TextView)findViewById(R.id.simple_ok_tv);
        mDownLl = (LinearLayout)findViewById(R.id.down_ll);
    }

    //设置取消是否可见
    public void setCancelVisiable() {
        mCancelTv.setVisibility(View.VISIBLE);
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
        mCancelTv.setOnClickListener(listener);
    }


    public void setContent(String content) {
        mContentTv.setVisibility(View.VISIBLE);
        mContentTv.setText(content);
    }

    //设置确定是否可见
    public void setSimpleOkVisiable() {
        mSimpleOkTv.setVisibility(View.VISIBLE);
        mSimpleOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    //设置确定是否可见
    public void setSimpleOkVisiable(View.OnClickListener listener) {
        mSimpleOkTv.setVisibility(View.VISIBLE);
        mSimpleOkTv.setOnClickListener(listener);
    }

    //设置确定是否可见
    public void setOkVisiable(View.OnClickListener listener) {
        mOkTv.setVisibility(View.VISIBLE);
        mOkTv.setOnClickListener(listener);
    }
    public void setLeftButton(String content){
        mCancelTv.setText(content);
    }
    public void setRightButton(String content){
        mOkTv.setText(content);
    }
}
