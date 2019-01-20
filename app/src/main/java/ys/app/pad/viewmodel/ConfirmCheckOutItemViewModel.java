package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;


import ys.app.pad.BR;
import ys.app.pad.Constants;
import ys.app.pad.event.RxManager;
import ys.app.pad.model.VipInfo;

/**
 * Created by xyy on 17-7-15.
 */

public class ConfirmCheckOutItemViewModel extends BaseObservable {


    private final RxManager rxManager;
    private VipInfo model;
    private Context mContext;

    public ObservableField<String> obAnimal1Name = new ObservableField<String>();
    public ObservableField<String> obAnimal2Name = new ObservableField<String>();
    public ObservableField<String> obAnimal3Name = new ObservableField<String>();
    public ObservableField<String> obAnimal4Name = new ObservableField<String>();
    public ObservableField<String> type = new ObservableField<String>();//是否挂单


    public ConfirmCheckOutItemViewModel(VipInfo model, Context context,boolean isGuadan) {
        this.model = model;
        this.mContext = context;
        if (isGuadan){
            type.set("挂单");
        }else{
            type.set("支付");
        }
        rxManager = new RxManager();
    }


    @Bindable
    public VipInfo getModel() {
        return model;
    }

    public void setModel(VipInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }

    public void clickPay(View view){
        rxManager.post(Constants.confirm_type_info,model);
    }

}