package ys.app.pad.utils;

import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by liuyin on 2017/11/3.
 */

public class ScannerGunWatcher implements TextWatcher {
    private EditText editText;
    private String editString;
    private ScannerGunCallBack callBack;

    public ScannerGunWatcher(EditText mEditText) {
        this.editText = mEditText;
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = editText.getInputType(); // backup the input type
                editText.setInputType(InputType.TYPE_NULL); // disable soft input
                editText.onTouchEvent(event); // call native handler
                editText.setInputType(inType); // restore input type
                editText.setHint("请扫描");
                return true;
            }
        });
    }
    private Handler handler = new Handler();

    /**
     * 延迟线程，看是否还有下一个字符输入
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            editText.setText("");
            editText.setHint("扫描枪扫描");
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            handler.removeCallbacks(delayRun);
            callBack.callBack(editString);

        }
    };

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (delayRun != null) {
            handler.removeCallbacks(delayRun);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!StringUtils.isEmpty(s.toString())) {
            editString = s.toString();
            //延迟800ms，如果不再输入字符，则执行该线程的run方法
            handler.postDelayed(delayRun, 500);
        }
    }

    public void setOnWatcherListener(ScannerGunCallBack callBack) {
        this.callBack = callBack;
    }

    public interface ScannerGunCallBack {
        void callBack(String string);
    }
}
