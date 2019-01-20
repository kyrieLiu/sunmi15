package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityTakeOutInventoryBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.TakeOutInventoryViewModel;

/**
 * Created by aaa on 2017/3/7.
 */

public class TakeOutInventoryActivity extends BaseActivity{
    
    private ActivityTakeOutInventoryBinding binding;
    private TakeOutInventoryViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_take_out_inventory);
        setBackVisiable();
        setTitle("出库");
        getDataFromIntent();
        setBgWhiteStatusBar();

    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        GoodInfo result = (GoodInfo) extras.getSerializable(Constants.intent_info);
        mViewModel = new TakeOutInventoryViewModel(this,binding,result);
        binding.setViewModel(mViewModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null){
            mViewModel.reset();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mViewModel.activityResult(requestCode, resultCode, data);
    }
}
