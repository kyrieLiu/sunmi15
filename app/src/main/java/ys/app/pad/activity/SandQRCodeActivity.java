package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.SandQRCodeBinding;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.OrderPayParam;
import ys.app.pad.viewmodel.SandQRCodeModel;

/**
 * 扫码支付
 */
public class SandQRCodeActivity extends BaseActivity {

    SandQRCodeBinding mBinding;
    SandQRCodeModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sand_qrcode);
        setBackVisiable();
        Intent intent = getIntent();
        OrderPayParam orderPayParam = (OrderPayParam) intent.getSerializableExtra(Constants.intent_info);
        String channelType=orderPayParam.getPayChannelTypeNo();
            if ("0201".equals(channelType) || "0205".equals(channelType)){
                setTitle("微信收款");
            }else if("0104".equals(channelType) || "0106".equals(channelType)){
                setTitle("支付宝收款");
            }else {
                setTitle("银联支付");
            }
        ChargeInfo chargeInfo= (ChargeInfo) intent.getSerializableExtra("chargeModel");
        mViewModel = new SandQRCodeModel(this,mBinding,chargeInfo);
        mBinding.setViewModel(mViewModel);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
       mViewModel.onBackPress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onDestroy();
    }
}
