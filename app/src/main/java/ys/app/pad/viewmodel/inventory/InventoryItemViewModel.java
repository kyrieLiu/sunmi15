package ys.app.pad.viewmodel.inventory;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.model.GoodTypeInfo;

/**
 * Created by aaa on 2017/3/3.
 */

public class InventoryItemViewModel extends BaseObservable{

    private GoodTypeInfo model;
    private Context mContext;

    public InventoryItemViewModel(GoodTypeInfo model, Context context) {
        this.model = model;
        this.mContext = context;
    }


    @Bindable
    public GoodTypeInfo getModel() {
        return model;
    }

    public void setModel(GoodTypeInfo model) {
        this.model = model;
    }
}
