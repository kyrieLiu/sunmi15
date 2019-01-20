package ys.app.pad.viewmodel.manage;

import android.databinding.ObservableField;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.ModifyBossPswActivity;
import ys.app.pad.databinding.ActivityModifyBossPswBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;

/**
 * Created by lvgeda on 2017/6/10.
 */

public class ModifyBossPswViewModel {
    private final ModifyBossPswActivity mActivity;
    private final ActivityModifyBossPswBinding mBinding;
    private final ApiClient<BaseResult> mClient;
    public ObservableField<String> obOldPsw = new ObservableField<>();
    public ObservableField<String> obNewPsw = new ObservableField<>();
    public ObservableField<String> obConfirmPsw = new ObservableField<>();

    public ModifyBossPswViewModel(ModifyBossPswActivity activity, ActivityModifyBossPswBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
    }


    public void clickOkBtn(View view) {


        Map<String, String> params = new HashMap<>();
        params.put("newPassword", obNewPsw.get());
        params.put("oldPassword", obOldPsw.get());
        params.put("shopId", SpUtil.getShopId() + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("equipmentId", SpUtil.getShopId());
        mActivity.showRDialog();
        mClient.reqApi("modifyBossPsw", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            mActivity.showToast("店长密码修改成功");
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
