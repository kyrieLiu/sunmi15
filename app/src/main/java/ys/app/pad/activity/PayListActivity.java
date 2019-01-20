package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityPayListBinding;
import ys.app.pad.viewmodel.PayListViewModel;

/**
 * Created by aaa on 2017/4/25.
 */

public class PayListActivity extends BaseActivity {

    private ActivityPayListBinding binding;
    private PayListViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_list);
        setBackVisiable();
        setTitle("取单");
        viewModel = new PayListViewModel(this, binding);
        binding.setViewModel(viewModel);
        setBgWhiteStatusBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}
