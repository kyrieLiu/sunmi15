package ys.app.pad.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.R;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.LoginInfo;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.SpUtil;

/**
 * Created by WBJ on 2018/6/1 14:46.
 */

public class UnitPriceDialog extends AppCompatDialog {

    private TextView mCancelTv;
    private TextView mOkTv;
    EditText mEtPrice;
    private UnitPriceListener mCallback;
    private ApiClient<BaseListResult<LoginInfo>> mApiClient;
    private Context context;

    public UnitPriceDialog(@NonNull Context context) {
        super(context);
        super.setContentView(R.layout.dialog_price_change);
        this.setCanceledOnTouchOutside(false);
        this.context=context;
        init();
    }

    private void init() {
        mCancelTv = (TextView) findViewById(R.id.cancel_tv);
        mOkTv = (TextView) findViewById(R.id.ok_tv);
        mEtPrice=(EditText)findViewById(R.id.content);
        setCancelVisiable();
    }

    private void setCancelVisiable() {
        mCancelTv.setVisibility(View.VISIBLE);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                UnitPriceDialog.this.dismiss();
            }
        });
    }

    public void setOkVisiable(UnitPriceListener callback) {
        mCallback = callback;
        mOkTv.setVisibility(View.VISIBLE);
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price = mEtPrice.getText().toString().trim();
                mCallback.completed(price);
                UnitPriceDialog.this.dismiss();
                closeKeyboard();
            }
        });
    }


    public interface UnitPriceListener {
        /**
         * 改变价格
         * @param price
         */
        void completed(String price);
    }


    private void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
