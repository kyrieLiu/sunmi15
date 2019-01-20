package ys.app.pad.activity.vip;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityRefundVipBinding;
import ys.app.pad.viewmodel.vip.RefundVipViewModel;


public class RefundVipActivity extends BaseActivity {
    private ActivityRefundVipBinding binding;
    private RefundVipViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_refund_vip);
        setBackVisiable();
        setTitle("退卡");
        Intent intent=getIntent();
        long vipUserId=intent.getLongExtra("vipUserId",0);
        viewModel = new RefundVipViewModel(this,vipUserId,binding);
        binding.setViewModel(viewModel);
        setBgWhiteStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}
