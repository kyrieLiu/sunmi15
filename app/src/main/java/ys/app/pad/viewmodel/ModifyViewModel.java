package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.ModifyActivity;
import ys.app.pad.adapter.SelectAnimalTypeAdapter;
import ys.app.pad.adapter.SelectUnitAdapter;
import ys.app.pad.databinding.ActivityModifyBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.GoodUnitInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.DeleteDialog;
import ys.app.pad.widget.dialog.SelectDialog;

import static ys.app.pad.utils.AppUtil.formatStandardMoney;

/**
 * Created by aaa on 2017/3/8.
 */

public class ModifyViewModel extends BaseActivityViewModel {
    private ModifyActivity mActivity;
    private ActivityModifyBinding mBinding;
    private ApiClient<BaseResult> mClient;
    public GoodInfo mResult;
    public ObservableField<String> obName = new ObservableField<>();
    public ObservableField<String> obBarCode = new ObservableField<>();
    public ObservableLong obType = new ObservableLong();
    public ObservableLong obUnit = new ObservableLong();
    public ObservableField<String> obTypeName = new ObservableField<>();
    public ObservableField<String> obUnitName = new ObservableField<>();
    public ObservableField<String> obCostAmt = new ObservableField<>();
    public ObservableField<String> obSeeCostAmt = new ObservableField<>();
    public ObservableField<String> obRealAmt = new ObservableField<>();
    public ObservableField<String> obStockNum = new ObservableField<>();
    public ObservableField<String> obGoodsVipPrice = new ObservableField<>();//商品会员价
    private RxManager rxManager;
    private SelectDialog mSelectDialog;
    private ApiClient<BaseListResult<GoodTypeInfo>> mClientGoodTypeInfo;
    private ApiClient<BaseListResult<GoodUnitInfo>> mUnitClient;
    public ObservableBoolean getTypeHttpSuccess = new ObservableBoolean();
    public ObservableBoolean getUnitHttpSuccess = new ObservableBoolean();
    private List<GoodTypeInfo> mTypeData;
    private List<GoodUnitInfo> mUnitData = new ArrayList<>();
    private SelectDialog mUnitSelectDialog;
    private SelectUnitAdapter mSelectUnitAdapter;
    private DeleteDialog mConfirmDeleteDialog;


    public ModifyViewModel(ModifyActivity activity, ActivityModifyBinding binding, GoodInfo result) {
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
        obSeeCostAmt.set("查看");
        mBinding.etCostMoney.setEnabled(false);
        obBarCode.set(mResult.getBarCode().equals(Constants.barCodeTemp_int)?"":mResult.getBarCode());
        obType.set(mResult.getType());
        obTypeName.set(mResult.getTypeName());
        obUnitName.set(mResult.getUnit());
        obRealAmt.set(formatStandardMoney(mResult.getRealAmt()));
        obGoodsVipPrice.set(formatStandardMoney(mResult.getVipAmt()));
        obStockNum.set(mResult.getStockNum() + "");

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
                            obCostAmt.set(formatStandardMoney(split[0] + "." + split[1].substring(0, 2)));
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
//        params.put("shopId", mResult.getId() + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        mClientGoodTypeInfo.reqApi("goodsType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodTypeInfo> info) {
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

    /**
     * 获取单位
     */
    public void getUnitHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");

        mUnitClient.reqApi("goodsUnit", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodUnitInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodUnitInfo> info) {
                        if (info.isSuccess()) {
                            getUnitHttpSuccess.set(true);
                            mUnitData = info.getData();
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

    /**
     * 查看成本价需要店长密码
     */
    public void verPassword(){
        if (mConfirmDeleteDialog == null){
            mConfirmDeleteDialog = new DeleteDialog(mActivity);
        }
        mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
            @Override
            public void verificationPwd(boolean b) {
                if (b){
                    if (mConfirmDeleteDialog != null){
                        mConfirmDeleteDialog.dismiss();
                    }
                    obSeeCostAmt.set("元");
                    mBinding.etCostMoney.setEnabled(true);
                    obCostAmt.set( AppUtil.formatStandardMoney(mResult.getCostAmt())+"");
                }else {
                    Toast.makeText(mActivity,"密码输入错误请重新输入",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDismiss() {
                mConfirmDeleteDialog = null;
            }
        });
        mConfirmDeleteDialog.show();
    }

    /**
     * 扫一扫
     *
     * @param v
     */
    public void clickScan(View v) {
    }




    public void clickButton(View v) {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("shopId",SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("isPromotion", mResult.getIsPromotion()+"");
        if (mResult.getIsPromotion()==1){
            params.put("promotionType",mResult.getPromotionType()+"");
            params.put("promotionNum",mResult.getPromotionNum()+"");
        }
        params.put("id", mResult.getId() + "");
        params.put("name", obName.get());
        String realAmt=obRealAmt.get();
        if (realAmt!=null){
            realAmt=realAmt.replaceAll(",","");
        }
        params.put("realAmt", realAmt);
        params.put("stockNum", obStockNum.get());
        params.put("type", obType.get() + "");
        params.put("typeName", obTypeName.get());
        params.put("unit", obUnitName.get());


        if(TextUtils.isEmpty(obBarCode.get())){
            params.put("barCode",Constants.barCodeTemp_int);
        }else {
            params.put("barCode", obBarCode.get());
        }
        if(TextUtils.isEmpty(obCostAmt.get())){
            params.put("costAmt", mResult.getCostAmt()+"");
        }else {
            String costAmt=obCostAmt.get();
            if (costAmt!=null){
                costAmt=costAmt.replaceAll(",","");
            }
            params.put("costAmt", costAmt);
        }
        if (StringUtils.isEmpty(obGoodsVipPrice.get())){
            params.put("vipAmt", obRealAmt.get().replaceAll(",",""));
        }else{
            params.put("vipAmt",obGoodsVipPrice.get().replaceAll(",",""));
        }
        mClient.reqApi("updateGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (rxManager == null) {
                                rxManager = new RxManager();
                            }
                            rxManager.post(Constants.bus_type_http_result, Constants.type_updateGoods_success);
                            Intent intent=new Intent();
                            mActivity.setResult(10,intent);
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
            SelectAnimalTypeAdapter adapter = new SelectAnimalTypeAdapter(mActivity, mTypeData);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    GoodTypeInfo info = mTypeData.get(position);
                    obType.set(info.getId());
                    obTypeName.set(info.getName());
                }
            });
        } else {
            mSelectDialog.setData(mTypeData);
        }
        mSelectDialog.show();
    }

    /**
     * 选择单位
     */
    public void selectUnitDialog() {

        if (mUnitSelectDialog == null) {
            mSelectUnitAdapter = new SelectUnitAdapter(mActivity, mUnitData);
            mUnitSelectDialog = new SelectDialog(mActivity, mSelectUnitAdapter);
            mUnitSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    GoodUnitInfo goodUnitInfo = mUnitData.get(position);
                    obUnitName.set(goodUnitInfo.getName());
                    obUnit.set(goodUnitInfo.getId());
                }
            });
        }
        mUnitSelectDialog.show();
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
        mSelectDialog = null;
        mUnitSelectDialog = null;
        mSelectUnitAdapter = null;
        mTypeData = null;
        mUnitData = null;
        mConfirmDeleteDialog=null;
    }
}
