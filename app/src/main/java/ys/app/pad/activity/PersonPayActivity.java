package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.PersonalPayBinding;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.viewmodel.PersonalPayModel;

public class PersonPayActivity extends BaseActivity {

    private PersonalPayBinding binding;
    private PersonalPayModel payModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_personal_pay);
        setBackVisiable();

        Intent intent=getIntent();
        String  pay_type=intent.getStringExtra(Constants.intent_flag);
        String money=intent.getStringExtra("money");
        String intentFrom=intent.getStringExtra(Constants.intent_name);
        String orderNO=intent.getStringExtra(Constants.intent_orderNo);
        if ("0112".equals(pay_type)){
            setTitle("微信收款");
        }else if ("0113".equals(pay_type)){
            setTitle("支付宝收款");
        }else if ("1001".equals(pay_type)){
            setTitle("现金收款");
        }else if ("1006".equals(pay_type)){
            setTitle("刷卡收款");
        }
        ChargeInfo chargeInfo= (ChargeInfo) intent.getSerializableExtra("chargeModel");
        payModel = new PersonalPayModel(this,binding,pay_type,money,intentFrom,chargeInfo,orderNO);
        binding.setViewModel(payModel);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        payModel.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Constants.result_code);
        finish();
    }

    @Override
    protected void onPause() {//如果存在activity跳转，需要做清理操作
        super.onPause();
        payModel.onPause();
    }
}
