package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import java.util.List;

import ys.app.pad.BR;
import ys.app.pad.model.InventoryRecordsInfo;

/**
 * Created by admin on 2017/7/12.
 */

public class ItemInventoryRecordItemViewModel extends BaseObservable {

    private InventoryRecordsInfo model;
    private Context mContext;
    public ObservableField<List<InventoryRecordsInfo.InventoryList2Bean>> listData = new ObservableField<>();


    public ItemInventoryRecordItemViewModel(InventoryRecordsInfo model, Context context) {
        this.model = model;
        this.mContext = context;
        listData.set(model.getInventoryList2());
    }

    @Bindable
    public InventoryRecordsInfo getModel() {
        return model;
    }

    public void setModel(InventoryRecordsInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
        listData.set(model.getInventoryList2());
    }
}
