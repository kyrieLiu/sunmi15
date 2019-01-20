package ys.app.pad.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.Constants;
import ys.app.pad.activity.FosterCareActivity;
import ys.app.pad.activity.HairdressingActivity;
import ys.app.pad.activity.LivingBuyActivity;
import ys.app.pad.model.SummitOrderInfo;


/**
 * 作者：lv
 * 时间：2017/4/3 18:42
 */

public class ShoppingCarAdapterViewModel extends BaseObservable {

    public   SummitOrderInfo model;
    private Activity mContext;
    public ObservableField<String> confirmText = new ObservableField<>();
    public ObservableBoolean seeDetail=new ObservableBoolean(false);


    public ShoppingCarAdapterViewModel(Activity context, SummitOrderInfo info){
        this.mContext = context;
        this.model = info;
        setConfirmData();
    }

    @Bindable
    public SummitOrderInfo getModel() {
        return model;
    }

    public void setModel(SummitOrderInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
        setConfirmData();
    }
    private void setConfirmData(){
        if ("寄养".equals(model.getProductTypeName())||"美容".equals(model.getProductTypeName())){
            seeDetail.set(true);
            if ("寄养".equals(model.getProductTypeName())){
                confirmText.set("寄养协议");
            }else{
                confirmText.set("美容确认单");
            }
        }else if ("活体".equals(model.getProductTypeName())){
            seeDetail.set(true);
            confirmText.set("宠物买卖合同");
        } else{
            seeDetail.set(false);
        }
    }
    public void clickDetail(){
        Intent intent =new Intent();
        if ("寄养".equals(model.getProductTypeName())){
            intent.setClass(mContext,FosterCareActivity.class);
        }else if ("美容".equals(model.getProductTypeName())){
            intent.setClass(mContext,HairdressingActivity.class);
        } else{
            intent.setClass(mContext,LivingBuyActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.intent_info,model);
        intent.putExtras(bundle);
        mContext.startActivityForResult(intent,10);
    }
}
