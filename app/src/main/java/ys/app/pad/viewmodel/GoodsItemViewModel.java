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
import ys.app.pad.activity.AddInventoryActivity;
import ys.app.pad.activity.GoodsDetailActivity;
import ys.app.pad.activity.TakeOutInventoryActivity;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.DeleteDialog;

/**
 * Created by lyy on 2017/2/14 13:46.
 * email：2898049851@qq.com
 */

public class GoodsItemViewModel extends BaseObservable {

    private GoodInfo model;
    private Context mContext;
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> promotionPrice = new ObservableField<>();
    public ObservableBoolean promotionSetting = new ObservableBoolean(false);
    public ObservableBoolean ruku = new ObservableBoolean(false);
    public ObservableBoolean chuku = new ObservableBoolean(false);
    public ObservableBoolean isInventoryList=new ObservableBoolean(false);
    private CustomDialog mDialog;
    private ApiClient<BaseResult> mApiClient;
    private DeleteDialog mConfirmDeleteDialog;

    public GoodsItemViewModel(GoodInfo model, Context context,int intentFrom) {
        this.model = model;
        this.mContext = context;
        setData(intentFrom);
    }



    @Bindable
    public GoodInfo getModel() {
        return model;
    }

    public void setModel(GoodInfo model,int intentFrom) {
        this.model = model;
        setData(intentFrom);

        notifyPropertyChanged(BR.model);
    }

    private void setData(int intentFrom){
        setPrice(model);
        switch (intentFrom){
            case Constants.intent_form_shangpin_kucunliebiao:
                isInventoryList.set(true);
                break;
            case Constants.intent_form_shangpin_cuxiaoshezhi:
                promotionSetting.set(true);
                break;
            case Constants.intent_form_shangpin_ruku:
                this.ruku.set(true);
                break;
            case Constants.intent_form_shangpin_chuku:
                this.chuku.set(true);
                break;
        }
    }
    private void setPrice(GoodInfo model) {
        price.set(AppUtil.formatStandardMoney(model.getRealAmt()));
        promotionPrice.set(AppUtil.formatStandardMoney(model.getPromotionAmt()));
    }

    public void promotionClick() {

        if (model.getIsPromotion() == 1) {//从促销中移除
            if (mDialog == null) {
                mDialog = new CustomDialog((Activity) mContext);
                mDialog.setContent("该商品促销暂未到期,确定从促销中移除?");
                mDialog.setCancelVisiable();
                mDialog.setOkVisiable(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mApiClient == null){
                            mApiClient = new ApiClient<BaseResult>();
                        }
                        Map<String,String> params = new HashMap<>();
                        params.put("id", model.getId() + "");
                        params.put("shopId", model.getShopId());
                        params.put("branchId", SpUtil.getBranchId()+"");
                        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
                        params.put("isPromotion","0");
                        params.put("isFold","0");
                        mApiClient.reqApi("updateGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                                .updateUI(new Callback<BaseResult>() {
                                    @Override
                                    public void onSuccess(BaseResult baseResult) {
                                        if (baseResult.isSuccess()) {
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
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.intent_info, model);
            bundle.putString(Constants.intent_flag, "1");
            bundle.putBoolean(Constants.intent_promotion, true);
            intent.putExtras(bundle);
            mContext.startActivity(intent);

        }
    }

    public void kuClick(){

        showDialog();
    }

    private void showDialog() {

                if (mConfirmDeleteDialog == null){
                    mConfirmDeleteDialog = new DeleteDialog(mContext);
                }
                mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
                    @Override
                    public void verificationPwd(boolean b) {
                        if (b){
                            if (mConfirmDeleteDialog != null){
                                mConfirmDeleteDialog.dismiss();
                            }
                            if (ruku.get()){
                                //入库
                                Intent intent = new Intent(mContext, AddInventoryActivity.class);
                                intent.putExtra(Constants.intent_info, model);
                                mContext.startActivity(intent);

                            }

                            if (chuku.get()){
                                //出库
                                Intent intent = new Intent(mContext, TakeOutInventoryActivity.class);
                                intent.putExtra(Constants.intent_info, model);
                                mContext.startActivity(intent);
                            }
                        }else {
                            Toast.makeText(mContext,"密码输入错误请重新输入",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDismiss() {
                        mConfirmDeleteDialog = null;
                    }
                });
                mConfirmDeleteDialog.show();
            }





}
