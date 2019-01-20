package ys.app.pad.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.AddServiceActivity;
import ys.app.pad.adapter.SelectServiceTypeAdapter;
import ys.app.pad.databinding.ActivityAddServiceBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodsListResult;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.imagepicker.ImagePicker;
import ys.app.pad.widget.timeselector.Utils.TextUtil;

/**
 * Created by admin on 2017/6/7.
 */

public class AddServiceActivityViewModel extends BaseActivityViewModel{

    private  AddServiceActivity mActivity;
    private  ActivityAddServiceBinding mBinding;

    private ImagePicker mImagePicker;
    private ApiClient<BaseListResult<ServiceTypeInfo>> mClient;
    private ApiClient<BaseResult> mSummitClient;
    public GoodsListResult mResult;
    public ObservableField<String> obServiceTypeName = new ObservableField<>();
    public ObservableLong obServiceType = new ObservableLong();
    public ObservableField<String> obServiceRealPrice = new ObservableField<>();
    public ObservableField<String> obServiceName = new ObservableField<>();
    public ObservableField<String> obServiceVipPrice = new ObservableField<>();

    public ObservableBoolean getTypeHttpSuccess = new ObservableBoolean();
    private RxManager mRxManager;
    private SelectServiceTypeAdapter mSelectTypeAdapter;
    private List<ServiceTypeInfo> mTypeData = new ArrayList<>();
    private SelectDialog mTypeSelectDialog;
    private ServiceTypeInfo mTypeInfo;
    public ObservableField<Uri> imgUri = new ObservableField<>();
    private String imgUrlHttp = "img";
    public ObservableField<byte[]> imgData = new ObservableField<>();


    public AddServiceActivityViewModel(AddServiceActivity activity, ActivityAddServiceBinding binding,ImagePicker picker) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
        this.mSummitClient = new ApiClient<>();
        this.mImagePicker = picker;
    }

    public TextWatcher changedListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable == null) return;
            String s = editable.toString();
            if (!StringUtils.isEmptyOrWhitespace(s)) {
                if (s.length() > 8) {
                    Toast.makeText(mActivity, "不能超过八位数", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (s.contains(".")) {
                    String[] split = s.split("\\.");
                    if (split.length > 1 && !StringUtils.isEmptyOrWhitespace(split[1])) {
                        if (split[1].length() > 2) {
                            Toast.makeText(mActivity, "最多输入两位小数", Toast.LENGTH_SHORT).show();
                            obServiceRealPrice.set(split[0] + "." + split[1].substring(0, 2));
                            return;
                        }
                    }
                }
            }
        }
    };

    /**
     * 提交服务
     */
    private void summitHttp() {
        if (StringUtils.isEmptyOrWhitespace(obServiceName.get())) {
            showToast(mActivity, "服务名称不能为空");
            return;
        }
        if (StringUtils.isEmptyOrWhitespace(obServiceTypeName.get())) {
            showToast(mActivity, "类型不能为空");
            return;
        }
        if (StringUtils.isEmptyOrWhitespace(obServiceRealPrice.get())) {
            showToast(mActivity, "销售价不能为空");
            return;
        }
        if (StringUtils.isEmptyOrWhitespace(imgUrlHttp)) {
            showToast(mActivity, "请上传图片");
            return;
        }
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("name", obServiceName.get());
        params.put("typeName", obServiceTypeName.get());
        params.put("type", obServiceType.get() + "");
        params.put("realAmt", obServiceRealPrice.get());
        params.put("imgpath", imgUrlHttp);
        if (TextUtil.isEmpty(obServiceVipPrice.get())){
            params.put("vipAmt", obServiceRealPrice.get());
        }else{
            params.put("vipAmt", obServiceVipPrice.get());
        }

        mSummitClient.reqApi("addService", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult info) {
                        mActivity.hideRDialog();
                        if (info.isSuccess()) {
//                            if (mRxManager == null) {
//                                mRxManager = new RxManager();
//                            }
//                            mRxManager.post(Constants.bus_type_http_result, Constants.type_addService_success);
                            mActivity.showToast("添加成功");
                            mActivity.finish();
                        } else {
                            mActivity.showToast(info.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });
    }


    public void clickSelectTypeButton(View v) {
        selectTypeDialog();
    }

    /**
     * 选择类型
     */
    private void selectTypeDialog() {
        if (mTypeSelectDialog == null) {
            mSelectTypeAdapter = new SelectServiceTypeAdapter(mActivity, mTypeData);
            mTypeSelectDialog = new SelectDialog(mActivity, mSelectTypeAdapter);
            mTypeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    mTypeInfo = mTypeData.get(position);
                    obServiceType.set(mTypeInfo.getId());
                    obServiceTypeName.set(mTypeInfo.getName());
                }
            });
        }

        mTypeSelectDialog.show();
    }

    public void getTypeHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mClient.reqApi("serviceType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceTypeInfo> info) {
                        if (info.isSuccess()) {
                            getTypeHttpSuccess.set(true);
                            mTypeData = info.getData();
                        } else {
                            showToast(mActivity, info.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    public void uploadClick() {
    }




    public void clickButton(View v){
//        upload();
        summitHttp();
    }

    public void reset() {
        mActivity = null;
        if (mClient != null){
            mClient.reset();
            mClient = null;
        }
        if (mSummitClient != null){
            mSummitClient.reset();
            mSummitClient = null;
        }
        if (mRxManager != null){
            mRxManager.clear();
            mRxManager = null;
        }
        mSelectTypeAdapter = null;
        mTypeData = null;
        mTypeSelectDialog = null;
        mTypeInfo = null;
        mResult = null;
    }
}
