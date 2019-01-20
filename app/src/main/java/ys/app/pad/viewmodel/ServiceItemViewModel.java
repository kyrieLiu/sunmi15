package ys.app.pad.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.ServiceDetailActivity;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.widget.dialog.CustomDialog;

/**
 * Created by lyy on 2017/2/21 16:22.
 * email：2898049851@qq.com
 */

public class ServiceItemViewModel extends BaseObservable {

    private ServiceInfo model;
    private Context mContext;
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> promotionPrice = new ObservableField<>();
    public ObservableBoolean promotionSetting = new ObservableBoolean(false);
    private CustomDialog mDialog;
    private  ApiClient<BaseResult> mSummitClient;



    public ServiceItemViewModel(ServiceInfo model, Context context, boolean promotion)
    {
        this.model = model;
        this.mContext = context;
        promotionSetting.set(promotion);
        setPrice(model);
    }

    private void setPrice(ServiceInfo model) {
        price.set(AppUtil.formatStandardMoney(model.getRealAmt()));
        promotionPrice.set(AppUtil.formatStandardMoney(model.getPromotionAmt()));
    }

    @Bindable
    public ServiceInfo getModel() {
        return model;
    }

    public void setModel(ServiceInfo model, boolean promotion) {
        this.model = model;
        setPrice(model);
        promotionSetting.set(promotion);
        notifyPropertyChanged(BR.model);
    }

    public void promotionClick() {

        if (model.getIsPromotion() == 1) {//从促销中移除
            if (mDialog == null) {
                mDialog = new CustomDialog((Activity) mContext);
                mDialog.setContent("该服务促销暂未到期,确定从促销中移除?");
                mDialog.setCancelVisiable();
                mDialog.setOkVisiable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mSummitClient == null){
                            mSummitClient = new ApiClient<BaseResult>();
                        }
                        Map<String,String> params = new HashMap<>();
                        params.put("id", model.getId() + "");
                        params.put("shopId", model.getShopId());
                        params.put("branchId", SpUtil.getBranchId()+"");
                        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
                        params.put("isPromotion","0");
                        params.put("isFold","0");
                        mSummitClient.reqApi("updateService", params, ApiRequest.RespDataType.RESPONSE_JSON)
                                .updateUI(new Callback<BaseResult>() {
                                    @Override
                                    public void onSuccess(BaseResult baseResult) {
                                        if (baseResult.isSuccess()){
                                            mDialog.dismiss();
                                            model.setIsPromotion(0);
                                        }else {
                                            Toast.makeText(mContext,baseResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        super.onError(e);
                                    }
                                });

                    }
                });
            }
            mDialog.show();

        } else {//加入促销
            Intent intent = new Intent(mContext, ServiceDetailActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtra(Constants.intent_service_info, model);
            bundle.putBoolean(Constants.intent_promotion, true);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        }
    }

}
