package ys.app.pad.widget.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.LoginInfo;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.StringUtils;

/**
 * Created by xyy on 17-7-16.
 */

public class DeleteDialog extends AppCompatDialog {

    private TextView mCancelTv;
    private TextView mOkTv;
    private EditText mPwdEt,mEtNum;
    private TextInputLayout til;
    private IDeleteDialogCallback mCallback;
    private ApiClient<BaseListResult<LoginInfo>> mApiClient;
    private Context context;


    public DeleteDialog(@NonNull Context context) {
        super(context, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.dialog_delete);
        this.context=context;
        init();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCallback != null) {
            mCallback.onDismiss();
        }
    }

    private void init() {
        mCancelTv = (TextView) findViewById(R.id.cancel_tv);
        mOkTv = (TextView) findViewById(R.id.ok_tv);
        mPwdEt = (EditText) findViewById(R.id.etPwd);
        mEtNum=(EditText)findViewById(R.id.etNum);
        setCancelVisiable();
        mApiClient = new ApiClient<BaseListResult<LoginInfo>>();
    }

    public void setCancelVisiable() {
        mCancelTv.setVisibility(View.VISIBLE);
        mCancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
                DeleteDialog.this.dismiss();
            }
        });
    }

    public void setOkVisiable(final IDeleteDialogCallback callback) {
        mCallback = callback;
        mOkTv.setVisibility(View.VISIBLE);
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (mApiClient == null) mApiClient = new ApiClient<BaseListResult<LoginInfo>>();
                    
                    Map<String, String> params = new HashMap<>();
                    params.put("passWord", mPwdEt.getText().toString());
                    params.put("shopId", SpUtil.getShopId() );
                    params.put("branchId",SpUtil.getBranchId()+"");
                    params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
                    params.put("equipmentId", SpUtil.getShopId());
                    showRDialog();
                    mApiClient.reqApi("userLoginBoss", params, ApiRequest.RespDataType.RESPONSE_JSON)
                            .updateUI(new Callback<BaseListResult<LoginInfo>>() {
                                @Override
                                public void onSuccess(BaseListResult<LoginInfo> baseResult) {
                                    hideRDialog();
                                    if (mCallback != null && baseResult != null && baseResult.isSuccess()) {
                                        closeKeyboard();
                                        mCallback.verificationPwd(true);
                                    } else if (callback != null) {
                                        callback.verificationPwd(false);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    Logger.e("",e);
                                    hideRDialog();
                                    if (callback != null) {
                                        callback.verificationPwd(false);
                                    }
                                }
                            });

            }
        });
    }

    /**
     * 设置提示语
     * @param hint
     */
    public void setHint(String hint){
        mEtNum.setHint(hint);
    }
    public void setOnConfirmListener(final OnOkConfirmListener listener){
        mOkTv.setVisibility(View.VISIBLE);
        mEtNum.setVisibility(View.VISIBLE);
        mPwdEt.setVisibility(View.GONE);
        mOkTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             String result=mEtNum.getText().toString();
             if (StringUtils.isEmpty(result)){
                 Toast.makeText(context,"请输入赠送数量",Toast.LENGTH_SHORT).show();
                 return;
             }
             listener.confirmResult(result);
            }
        });
    }
    private RequestDialog mRDialog;
    public void showRDialog() {
        if (mRDialog == null) {
            mRDialog = new RequestDialog(getContext());
        }
            mRDialog.show();

    }

    public void hideRDialog() {
        if (mRDialog != null) {
                mRDialog.hide();
                mRDialog.dismiss();
        }
    }

    public interface IDeleteDialogCallback {
        /**
         * 验证结果
         * @param b
         */
        void verificationPwd(boolean b);

        /**
         *
         * 对话框消失
         */
        void onDismiss();
    }
    public interface OnOkConfirmListener{
        void confirmResult(String result);
    }

    private void closeKeyboard(){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
