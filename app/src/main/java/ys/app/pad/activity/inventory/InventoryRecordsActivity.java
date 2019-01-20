package ys.app.pad.activity.inventory;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.InventoryRecordsBinding;
import ys.app.pad.viewmodel.inventory.InventoryRecordsViewModel;

public class InventoryRecordsActivity extends BaseActivity {

    private InventoryRecordsBinding mBinding;
    private InventoryRecordsViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_records);
        setBackVisiable();
        setTitle("盘点记录");
        mViewModel = new InventoryRecordsViewModel(this,mBinding);
        mBinding.setViewModel(mViewModel);
        mViewModel.init();
        setBgWhiteStatusBar();
    }
}
