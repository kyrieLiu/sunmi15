package ys.app.pad.viewmodel.inventory;

import android.databinding.ObservableField;

import java.util.List;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.activity.inventory.InventoryRecordsDetailActivity;
import ys.app.pad.databinding.InventoryRecordsDetailBinding;
import ys.app.pad.model.InventoryRecordsInfo;

/**
 * Created by admin on 2017/7/12.
 */

public class InventoryRecordsDetailViewModel extends BaseActivityViewModel{

    private InventoryRecordsDetailActivity mActivity;
    private InventoryRecordsDetailBinding mBinding;
    public ObservableField<List<InventoryRecordsInfo.InventoryListBean>> listData = new ObservableField<>();

    public InventoryRecordsDetailViewModel(InventoryRecordsDetailActivity activity, InventoryRecordsDetailBinding binding){
        this.mActivity = activity;
        this.mBinding = binding;
    }

    public void init(InventoryRecordsInfo info){
        listData.set(info.getInventoryList());
    }
}
