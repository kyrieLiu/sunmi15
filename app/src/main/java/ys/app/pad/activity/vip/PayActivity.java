package ys.app.pad.activity.vip;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityPayBinding;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.viewmodel.vip.PayViewModel;

/**
 * Created by aaa on 2017/3/1.
 */

public class PayActivity extends BaseActivity {

    private ActivityPayBinding binding;
    private PayViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pay);
        setBackVisiable();
        setTitle("充值结算");

       ChargeInfo chargeInfo = (ChargeInfo)getIntent().getSerializableExtra(Constants.intent_info);
        mViewModel = new PayViewModel(this,binding);
        binding.setViewModel(mViewModel);
        mViewModel.setIntentData(chargeInfo);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    public void onBackPressed() {
        mViewModel.onBackPress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constants.request_code){
            mViewModel.onActivityResult();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.permission_request_code) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                mViewModel.toScanActivity();
            }
        }
    }
}
