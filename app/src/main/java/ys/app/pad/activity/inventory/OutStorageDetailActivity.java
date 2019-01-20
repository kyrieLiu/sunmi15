package ys.app.pad.activity.inventory;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.OutStorageDetialBinding;
import ys.app.pad.model.OutStorageInfo;


public class OutStorageDetailActivity extends BaseActivity {

    OutStorageDetialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_out_storage_detail);
        setBackVisiable();
        setTitle();
         OutStorageInfo info = (OutStorageInfo) getIntent().getSerializableExtra(Constants.intent_info);
        binding.setDataBean(info);
        setBgWhiteStatusBar();
    }

    private void setTitle() {
        int  shopType=getIntent().getIntExtra(Constants.intent_flag, 0);
        switch (shopType) {
            case 1:
                setTitle("入库详情");
                break;
            case 2:
                setTitle("出库详情");
                break;
        }
    }
}
