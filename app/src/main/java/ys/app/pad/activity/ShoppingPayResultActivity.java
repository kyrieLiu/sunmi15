package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityShoppingPayResultBinding;
import ys.app.pad.viewmodel.ShoppingPayResultViewModel;

/**
 * Created by aaa on 2017/3/16.
 */
public class ShoppingPayResultActivity extends BaseActivity {

    private ActivityShoppingPayResultBinding binding;
    private ShoppingPayResultViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_shopping_pay_result);
        setBackVisiable();
        setTitle("支付结果");
        mViewModel = new ShoppingPayResultViewModel(this, binding);
        binding.setViewModel(mViewModel);
        Intent intent = getIntent();
        String orderNo = intent.getStringExtra(Constants.intent_orderNo);
        mViewModel.setIntentData(orderNo);
        //setBgWhiteStatusBar();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mViewModel.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }
}
