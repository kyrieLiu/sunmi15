package ys.app.pad.activity.vip;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import java.io.Serializable;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityChargeBinding;
import ys.app.pad.model.VipInfo;
import ys.app.pad.viewmodel.ChargeViewModel;

/**
 * Created by aaa on 2017/3/1.
 */

public class ChargeActivity extends BaseActivity {

    private ActivityChargeBinding binding;
    private ChargeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_charge);
        setBackVisiable();
        setTitle("充值");

        mViewModel = new ChargeViewModel(this,binding);
        binding.setViewModel(mViewModel);

        long vipUserID=getIntent().getLongExtra(Constants.intent_id,-1);
        if (vipUserID!=-1) mViewModel.setVipId(vipUserID);
        Serializable serializable=getIntent().getSerializableExtra(Constants.intent_info);
        if (serializable!=null){
            VipInfo vipInfo= (VipInfo) serializable;
            mViewModel.setVipIntentData(vipInfo);
        }

        mViewModel.init();
        setBgWhiteStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }

}
