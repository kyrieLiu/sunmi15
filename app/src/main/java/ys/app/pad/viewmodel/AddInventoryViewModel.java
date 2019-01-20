package ys.app.pad.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.AddInventoryActivity;
import ys.app.pad.databinding.ActivityAddInventoryBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.timeselector.TimeSelector;

/**
 * Created by aaa on 2017/3/7.
 */
public class AddInventoryViewModel extends BaseActivityViewModel {

    private final AddInventoryActivity mActivity;
    private final ActivityAddInventoryBinding mBinding;
    public GoodInfo mResult;
    public ObservableField<String> obAddInventory = new ObservableField<>();
    public ObservableBoolean obButtonEnable = new ObservableBoolean(false);
    private ApiClient<BaseResult> mClient;
    private RxManager mRxManager;
    public ObservableField<String> obGoodsProductDate = new ObservableField<>();//商品生产日期

    public AddInventoryViewModel(AddInventoryActivity activity, ActivityAddInventoryBinding binding, GoodInfo result) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mResult = result;
        this.mClient = new ApiClient<>();
        this.mRxManager = new RxManager();
    }

    public void clickButton(View v) {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        String num = obAddInventory.get();
        if(StringUtils.isEmptyOrWhitespace(obAddInventory.get())){
            params.put("num", mResult.getStockNum()+"");
        }else {
            params.put("num", mResult.getStockNum()+Integer.parseInt(num)+"");
        }
        params.put("stockNum",num);
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("id",mResult.getId()+"");
        params.put("type", "1");

        mClient.reqApi("updateInvertory", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            mRxManager.post(Constants.bus_type_http_result,Constants.type_updateGoods_success);
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

    public TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean b4 = !StringUtils.isEmptyOrWhitespace(obAddInventory.get());
            if (b4 && s != null) {
                String s1 = s.toString();
                int add = -1;
                if (!TextUtils.isEmpty(s1)) {
                    try{
                        add = Integer.parseInt(s1);
                    }catch (Exception e){

                    }
                    if (add > 0){
                        obButtonEnable.set(true);
                    }else {
                        obButtonEnable.set(false);
                    }
                }
            }
        }
    };

    public void manufactureTimeClick() {
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obGoodsProductDate.set(time);
            }
        }, "2015-1-1 00:00", "2050-12-31 24:00");
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }
}
