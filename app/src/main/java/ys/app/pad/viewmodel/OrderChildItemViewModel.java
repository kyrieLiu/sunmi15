package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.model.OrderInfo;
import ys.app.pad.utils.AppUtil;

/**
 * Created by admin on 2017/4/25.
 */

public class OrderChildItemViewModel extends BaseObservable {


    private OrderInfo.OrderDetailedListBean model;
    private Context mContext;
    public ObservableField<String> vipPrice = new ObservableField<>();
    public ObservableField<String> price = new ObservableField<>();


    public OrderChildItemViewModel(OrderInfo.OrderDetailedListBean model, Context context)
    {
        this.model = model;
        this.mContext = context;
        setVipPrice(model);
    }

    private void setVipPrice(OrderInfo.OrderDetailedListBean model) {
        vipPrice.set(AppUtil.formatStandardMoney(model.getVipPrice()));
        price.set(AppUtil.formatStandardMoney(model.getPrice()));
    }


    @Bindable
    public OrderInfo.OrderDetailedListBean getModel() {
        return model;
    }

    public void setModel(OrderInfo.OrderDetailedListBean model) {
        this.model = model;
        setVipPrice(model);
        notifyPropertyChanged(BR.model);
    }


}
