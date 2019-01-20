package ys.app.pad.viewmodel.inventory;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.BR;
import ys.app.pad.model.InventoryRecordsInfo;

/**
 * Created by admin on 2017/7/12.
 */

public class InventoryRecordDetailViewModel extends BaseObservable {

    private Context mContext;
    public InventoryRecordsInfo.InventoryListBean model;

    public InventoryRecordDetailViewModel(InventoryRecordsInfo.InventoryListBean bean, Context context) {
        this.mContext = context;
        this.model = bean;
    }

    @Bindable
    public InventoryRecordsInfo.InventoryListBean getModel() {
        return model;
    }

    public void setModel(InventoryRecordsInfo.InventoryListBean model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }
}
