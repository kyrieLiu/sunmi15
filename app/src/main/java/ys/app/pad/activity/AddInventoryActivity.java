package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAddInventoryBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.AddInventoryViewModel;

/**
 * Created by aaa on 2017/3/7.
 */

public class AddInventoryActivity extends BaseActivity {

    private ActivityAddInventoryBinding binding;
    private AddInventoryViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_inventory);
        setBackVisiable();
        setTitle("入库");
        getDataFromIntent();
        setBgWhiteStatusBar();

    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        GoodInfo result = (GoodInfo) extras.getSerializable(Constants.intent_info);
        mViewModel = new AddInventoryViewModel(this,binding,result);
        binding.setViewModel(mViewModel);
    }
}
