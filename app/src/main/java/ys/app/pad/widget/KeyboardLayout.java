package ys.app.pad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ys.app.pad.R;
import ys.app.pad.callback.KeyboardListener;

/**
 * Created by aaa on 2017/3/14.
 */

public class KeyboardLayout extends FrameLayout implements View.OnClickListener {

    private  TextView mPointTv;
    private  TextView mOkTv;
    private  RelativeLayout mDeleteRl;
    private  TextView mTv0;
    private  TextView mTv1;
    private  TextView mTv2;
    private  TextView mTv3;
    private  TextView mTv4;
    private  TextView mTv5;
    private  TextView mTv6;
    private  TextView mTv7;
    private  TextView mTv8;
    private  TextView mTv9;

    public void setListener(KeyboardListener listener) {
        this.listener = listener;
    }

    private KeyboardListener listener;

    public KeyboardLayout(Context context) {
        super(context);
    }

    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_keyboard, this, true);
        mTv0 = (TextView) findViewById(R.id.tv0);
        mTv1 = (TextView) findViewById(R.id.tv1);
        mTv2 = (TextView) findViewById(R.id.tv2);
        mTv3 = (TextView) findViewById(R.id.tv3);
        mTv4 = (TextView) findViewById(R.id.tv4);
        mTv5 = (TextView) findViewById(R.id.tv5);
        mTv6 = (TextView) findViewById(R.id.tv6);
        mTv7 = (TextView) findViewById(R.id.tv7);
        mTv8 = (TextView) findViewById(R.id.tv8);
        mTv9 = (TextView) findViewById(R.id.tv9);

        mDeleteRl = (RelativeLayout) findViewById(R.id.delete_rl);
        mOkTv = (TextView) findViewById(R.id.ok_tv);
        mPointTv = (TextView) findViewById(R.id.point_tv);

        mTv0.setOnClickListener(this);
        mTv1.setOnClickListener(this);
        mTv2.setOnClickListener(this);
        mTv3.setOnClickListener(this);
        mTv4.setOnClickListener(this);
        mTv5.setOnClickListener(this);
        mTv6.setOnClickListener(this);
        mTv7.setOnClickListener(this);
        mTv8.setOnClickListener(this);
        mTv9.setOnClickListener(this);
        mDeleteRl.setOnClickListener(this);
        mOkTv.setOnClickListener(this);
        mPointTv.setOnClickListener(this);





    }

    public KeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.custom_keyboard, this, true);
        mTv0 = (TextView) findViewById(R.id.tv0);
        mTv1 = (TextView) findViewById(R.id.tv1);
        mTv2 = (TextView) findViewById(R.id.tv2);
        mTv3 = (TextView) findViewById(R.id.tv3);
        mTv4 = (TextView) findViewById(R.id.tv4);
        mTv5 = (TextView) findViewById(R.id.tv5);
        mTv6 = (TextView) findViewById(R.id.tv6);
        mTv7 = (TextView) findViewById(R.id.tv7);
        mTv8 = (TextView) findViewById(R.id.tv8);
        mTv9 = (TextView) findViewById(R.id.tv9);

        mDeleteRl = (RelativeLayout) findViewById(R.id.delete_rl);
        mOkTv = (TextView) findViewById(R.id.ok_tv);
        mPointTv = (TextView) findViewById(R.id.point_tv);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv0:
                if(listener!=null){
                    listener.onNumberClick("0");
                }
            break;
            case R.id.tv1:
                if(listener!=null){
                    listener.onNumberClick("1");
                }
                break;
            case R.id.tv2:
                if(listener!=null){
                    listener.onNumberClick("2");
                }
                break;
            case R.id.tv3:
                if(listener!=null){
                    listener.onNumberClick("3");
                }
                break;
            case R.id.tv4:
                if(listener!=null){
                    listener.onNumberClick("4");
                }
                break;
            case R.id.tv5:
                if(listener!=null){
                    listener.onNumberClick("5");
                }
                break;
            case R.id.tv6:
                if(listener!=null){
                    listener.onNumberClick("6");
                }
                break;
            case R.id.tv7:
                if(listener!=null){
                    listener.onNumberClick("7");
                }
                break;
            case R.id.tv8:
                if(listener!=null){
                    listener.onNumberClick("8");
                }
                break;
            case R.id.tv9:
                if(listener!=null){
                    listener.onNumberClick("9");
                }
                break;
            case R.id.point_tv:
                if(listener!=null){
                    listener.onPointClick();
                }
                break;
            case R.id.delete_rl:
                if(listener!=null){
                    listener.onDeleteClick();
                }
                break;
            case R.id.ok_tv:
                if(listener!=null){
                    listener.onOkClick();
                }
                break;

        }
    }

}
