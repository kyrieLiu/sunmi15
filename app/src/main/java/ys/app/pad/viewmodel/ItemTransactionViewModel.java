package ys.app.pad.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.Constants;
import ys.app.pad.activity.TransactionDetailActivity;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.StringUtils;

/**
 * Created by admin on 2017/6/11.
 */

public class ItemTransactionViewModel extends BaseObservable {
    @Bindable
    public OrderInfo model;
    private Context mContext;
    public ObservableBoolean isRefund=new ObservableBoolean();
    public ObservableField<String> orderType=new ObservableField<>();
    public ObservableField<String> orderMoney=new ObservableField<>();

    public ItemTransactionViewModel(OrderInfo model, Context context)
    {
        this.model = model;
        this.mContext = context;
        setModel(model);
    }
    @Bindable
    public OrderInfo getModel() {
        return model;
    }

    public void setModel(OrderInfo model) {
        this.model = model;
        isRefund.set(model.getIsRefund()==1&&model.getRealAmt()>0);
        if (!StringUtils.isEmpty(model.getPreviousOrderNo())){
            orderType.set("退款记录");
        }else if ("会员充值".equals(model.getOrderInfo())&&model.getRealAmt()<0){
            orderType.set("会员退卡");
        }else{
            orderType.set(model.getOrderInfo());
        }
        if (model.getRealAmt()<0){
            double realAmt=0-model.getRealAmt();
            orderMoney.set("-¥"+AppUtil.formatStandardMoney(realAmt));
        }else{
            orderMoney.set("¥"+ AppUtil.formatStandardMoney(model.getRealAmt()));
        }
        notifyPropertyChanged(BR.model);
    }

    public void click(){
        Intent intent = new Intent(mContext,TransactionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.intent_transaction,model);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
