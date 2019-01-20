package ys.app.pad.viewmodel.manage;

import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.LoginActivity;
import ys.app.pad.activity.manage.ModifyPswActivity;
import ys.app.pad.databinding.ActivityModifyPswBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.utils.StringUtils;

/**
 * Created by aaa on 2017/3/10.
 */

public class ModifyPswViewModel extends BaseActivityViewModel {
    private ActivityModifyPswBinding mBinding;
    private ModifyPswActivity mActivity;
    private ApiClient<BaseResult> mClient;
    public ObservableField<String> obOldPsw = new ObservableField<>();
    public ObservableField<String> obNewPsw = new ObservableField<>();
    public ObservableField<String> obConfirmPsw = new ObservableField<>();
    private int intentFlag;

    public ModifyPswViewModel(ModifyPswActivity activity, ActivityModifyPswBinding binding,int intentFlag) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        this.mBinding = binding;
        this.intentFlag=intentFlag;
        if (intentFlag==1)mBinding.tilModifyPasswordOld.setVisibility(View.GONE);

    }

    public void clickButton(View view) {
        if (StringUtils.isEmpty(obNewPsw.get())||StringUtils.isEmpty(obConfirmPsw.get())){
            showToast(mActivity,"请输入新密码");
            return;
        }
        if (!obNewPsw.get().equals(obConfirmPsw.get())){
            showToast(mActivity,"两次新密码不一致");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("newPassword", obNewPsw.get());
        if (intentFlag==0){
            params.put("type","0");
            if (StringUtils.isEmpty(obOldPsw.get())){
                showToast(mActivity,"请输入旧密码");
                return;
            }
            params.put("oldPassword", obOldPsw.get());
        }else{
            params.put("type","1");
        }

        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("shopId", SpUtil.getShopId());
        mActivity.showRDialog();
        mClient.reqApi("modifyLoginPsw", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            mActivity.showToast("登陆密码修改成功,请重新登录");
                            Intent intent =new Intent(mActivity, LoginActivity.class);
                            mActivity.startActivity(intent);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });
    }
}