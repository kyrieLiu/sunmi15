package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.model.OrderInfo;

/**
 * Created by Administrator on 2017/6/8.
 */

public class ItemVipConsumeViewModel extends BaseObservable {
    @Bindable
    public OrderInfo model;
    private Context mContext;

    public ItemVipConsumeViewModel(OrderInfo model, Context context)
    {
        this.model = model;
        this.mContext = context;
    }



    @Bindable
    public OrderInfo getModel() {
        return model;
    }

    public void setModel(OrderInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }

}
