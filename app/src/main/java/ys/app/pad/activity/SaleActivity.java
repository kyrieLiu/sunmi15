package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivitySaleBinding;
import ys.app.pad.viewmodel.SaleViewModel;

/**
 * Created by aaa on 2017/6/9.
 */

public class SaleActivity extends BaseActivity {

    private ActivitySaleBinding binding;
    private SaleViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sale);
        setBackVisiable();
        setTitle("促销");

        mViewModel = new SaleViewModel(this, binding);
        binding.setViewModel(mViewModel);
        setBgWhiteStatusBar();

    }

}
