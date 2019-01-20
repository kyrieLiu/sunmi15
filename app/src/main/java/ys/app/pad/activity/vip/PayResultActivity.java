package ys.app.pad.activity.vip;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityPayResultBinding;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.viewmodel.vip.PayResultViewModel;

/**
 * Created by aaa on 2017/3/2.
 */

public class PayResultActivity extends BaseActivity {

    private ActivityPayResultBinding binding;
    private PayResultViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay_result);
        setBackVisiable();
        setTitle("充值结果");

        mViewModel = new PayResultViewModel(this,binding);
        binding.setViewModel(mViewModel);
        Intent intent = getIntent();
        ChargeInfo chargeInfo = (ChargeInfo)intent.getSerializableExtra(Constants.intent_info);
        String orderNo = intent.getStringExtra(Constants.intent_orderNo);
        mViewModel.setIntentData(chargeInfo,orderNo);
        setBgWhiteStatusBar();

    }

    @Override
    public void onBackPressed() {
        if (mViewModel != null && !mViewModel.obIsPaySuccess.get()){
            mViewModel.showDialog();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }
}
