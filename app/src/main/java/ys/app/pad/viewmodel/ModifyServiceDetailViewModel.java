package ys.app.pad.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.ModifyServiceDetailActivity;
import ys.app.pad.adapter.SelectServiceTypeAdapter;
import ys.app.pad.databinding.ModifyServiceDetailActivityBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodUnitInfo;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.SelectDialog;

import static ys.app.pad.utils.AppUtil.formatStandardMoney;

/**
 * Created by Administrator on 2017/6/9.
 */

public class ModifyServiceDetailViewModel extends BaseActivityViewModel {

    private ModifyServiceDetailActivity mActivity;
    private ModifyServiceDetailActivityBinding mBinding;
    private ApiClient<BaseResult> mClient;
    public ServiceInfo mResult;
    public ObservableField<String> obName = new ObservableField<>();
    public ObservableLong obType = new ObservableLong();
    public ObservableField<String> obTypeName = new ObservableField<>();
    public ObservableField<String> obRealAmt = new ObservableField<>();
    public ObservableField<String> obServiceVipPrice = new ObservableField<>();
    private RxManager rxManager;
    private SelectDialog mSelectDialog;
    private ApiClient<BaseListResult<ServiceTypeInfo>> mClientGoodTypeInfo;
    private ApiClient<BaseListResult<GoodUnitInfo>> mUnitClient;
    public ObservableBoolean getTypeHttpSuccess = new ObservableBoolean();
    public ObservableBoolean getUnitHttpSuccess = new ObservableBoolean();
    private List<ServiceTypeInfo> mTypeData;


    public ModifyServiceDetailViewModel(ModifyServiceDetailActivity activity, ModifyServiceDetailActivityBinding binding, ServiceInfo result) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mResult = result;
        this.mClient = new ApiClient<BaseResult>();
        this.mClientGoodTypeInfo = new ApiClient<>();
        this.mUnitClient = new ApiClient<>();
        setData();
    }

    private void setData() {
        obName.set(mResult.getName());
        obType.set(mResult.getType());
        obTypeName.set(mResult.getTypeName());
        obRealAmt.set(formatStandardMoney(mResult.getRealAmt()));
        obServiceVipPrice.set(formatStandardMoney(mResult.getVipAmt()));

    }

    public TextWatcher mChangedListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = editable.toString();
            if (!TextUtils.isEmpty(s)) {
                if (s.length() > 8) {
                    Toast.makeText(mActivity, "不能超过八位数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s.contains(".")) {
                    String[] split = s.split("\\.");
                    if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                        if (split[1].length() > 2) {
                            Toast.makeText(mActivity, "最多输入两位小数", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }
            }

        }
    };

    public TextWatcher mTCredTSChangedListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String s = editable.toString();
            if (!TextUtils.isEmpty(s)) {
                if (s.length() > 8) {
                    Toast.makeText(mActivity, "不能超过八位数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s.contains(".")) {
                    String[] split = s.split("\\.");
                    if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                        if (split[1].length() > 2) {
                            Toast.makeText(mActivity, "最多输入两位小数", Toast.LENGTH_SHORT).show();
                            obRealAmt.set(formatStandardMoney(split[0] + "." + split[1].substring(0, 2)));
                            return;
                        }
                    }
                }
            }

        }
    };

    /**
     * 获取服务类型
     */
    public void getTypeHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mClientGoodTypeInfo.reqApi("serviceType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceTypeInfo> info) {
                        if (info.isSuccess()) {
                            getTypeHttpSuccess.set(true);
                            mTypeData = info.getData();
                        } else {
                            mActivity.showToast(info.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.showToast("网络异常");
                    }
                });
    }


    public void clickButton(View v) {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("id", mResult.getId() + "");
        params.put("shopId", mResult.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("name", obName.get());
        String realAmt=obRealAmt.get();
        if (realAmt!=null){
            realAmt=realAmt.replaceAll(",","");
        }
        params.put("realAmt", realAmt);

        params.put("type", obType.get() + "");
        params.put("typeName", obTypeName.get());
        params.put("isPromotion",mResult.getIsPromotion()+"");
        if (mResult.getIsPromotion()==1){
            params.put("promotionType",mResult.getPromotionType()+"");
            params.put("promotionNum",mResult.getPromotionNum()+"");
        }
        if (StringUtils.isEmpty(obServiceVipPrice.get())){
            params.put("vipAmt", realAmt);
        }else{
            params.put("vipAmt",obServiceVipPrice.get().replaceAll(",",""));
        }

        mClient.reqApi("updateService", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (rxManager == null) {
                                rxManager = new RxManager();
                            }
                            mActivity.showToast("修改成功");
                            rxManager.post(Constants.type_update_service, null);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });


    }

    /**
     * 选择类型
     */
    public void clickSelectTypeButton() {
        if (mSelectDialog == null) {
            SelectServiceTypeAdapter adapter = new SelectServiceTypeAdapter(mActivity, mTypeData);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    ServiceTypeInfo info = mTypeData.get(position);
                    obType.set(info.getId());
                    obTypeName.set(info.getName());
                }
            });
        } else {
            mSelectDialog.setServiceData(mTypeData);
        }
        mSelectDialog.show();
    }

    public void onRest() {
        mActivity = null;
        if (mClient != null){
            mClient.reset();
            mClient = null;
        }
        if (mUnitClient != null){
            mUnitClient.reset();
            mUnitClient = null;
        }
        if (mClientGoodTypeInfo != null){
            mClientGoodTypeInfo.reset();
            mClientGoodTypeInfo = null;
        }
        if (rxManager != null){
            rxManager.clear();
            rxManager = null;
        }
        mTypeData = null;
        mSelectDialog = null;
    }
}
