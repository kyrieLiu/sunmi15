package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.model.VipRechargeInfo;

/**
 * Created by Administrator on 2017/6/8.
 */

public class ItemVipRechargeViewModel extends BaseObservable {
    @Bindable
    public VipRechargeInfo model;
    private Context mContext;

    public ItemVipRechargeViewModel(VipRechargeInfo model, Context context)
    {
        this.model = model;
        this.mContext = context;
    }



    @Bindable
    public VipRechargeInfo getModel() {
        return model;
    }

    public void setModel(VipRechargeInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }

}
