package ys.app.pad.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityCommitOrderBinding;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.viewmodel.CommitOrderViewModel;

/**
 * Created by aaa on 2017/3/15.
 */

public class CommitOrderActivity extends BaseActivity {

    private ActivityCommitOrderBinding binding;
    private CommitOrderViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_commit_order);
        setBackVisiable();
        setTitle("提交订单");
        Intent intent = getIntent();
        String idStr = intent.getStringExtra(Constants.intent_id);
        String userName = intent.getStringExtra("userName");
        String userPhone = intent.getStringExtra("userPhone");
        mViewModel = new CommitOrderViewModel(this, binding, userName, userPhone);
        binding.setViewModel(mViewModel);
        mViewModel.init();
        mViewModel.getDataFromIntent(idStr);
        setBgWhiteStatusBar();

    }



    @Override
    public void onBackPressed() {
        if (mViewModel != null) {
            mViewModel.back();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.T1mini.equals(Build.MODEL)) {
            AidlUtil.getInstance().setTextToT1mini(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mViewModel.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Constants.request_code){
            mViewModel.onActivityResult(requestCode,resultCode,data);
        }else if (requestCode==10){
            if (resultCode==10){
                finish();
            }

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
