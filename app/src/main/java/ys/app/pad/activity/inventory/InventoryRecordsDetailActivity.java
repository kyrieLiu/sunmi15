package ys.app.pad.activity.inventory;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.InventoryRecordsDetailBinding;
import ys.app.pad.model.InventoryRecordsInfo;
import ys.app.pad.viewmodel.inventory.InventoryRecordsDetailViewModel;

public class InventoryRecordsDetailActivity extends BaseActivity {

    private InventoryRecordsInfo mInfo;
    private InventoryRecordsDetailBinding mBinding;
    private InventoryRecordsDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_inventory_records_detail);
        mViewModel = new InventoryRecordsDetailViewModel(this,mBinding);
        mBinding.setViewModel(mViewModel);
        mInfo = (InventoryRecordsInfo)getIntent().getExtras().getSerializable("InventoryRecords");
        mViewModel.init(mInfo);
        setBackVisiable();
        setTitle("盘点详情");
        setBgWhiteStatusBar();

    }
}
