package ys.app.pad.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.SandPayCodeBinding;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.OrderPayParam;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.ScannerGunWatcher;
import ys.app.pad.viewmodel.SandPayCodeModel;

/**
 * 付款码
 */
public class ScannerGunPayCodeActivity extends BaseActivity {

    SandPayCodeBinding mBinding;
    SandPayCodeModel mViewModel;
    private EditText et_saomiaoqiang;
    private ObjectAnimator animator;
    private MainConnectUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_sand_pay_code);
        setBackVisiable();
        Intent intent = getIntent();
        OrderPayParam sandPayParam = (OrderPayParam) intent.getSerializableExtra(Constants.intent_info);
        if (null != sandPayParam) {
            utils= MainConnectUtils.getInstance().setContext(this);
            String channelType=sandPayParam.getPayChannelTypeNo();
            if ("0013".equals(channelType) || "0105".equals(channelType)){
                setTitle("支付宝收款");
                utils.sendTextMessage("请出示支付宝付款码","");
                mBinding.ivScanExample.setVisibility(View.VISIBLE);
                mBinding.ivScanExample.setImageResource(R.mipmap.alipay_scan_sample);
            }else if("0203".equals(channelType) || "0204".equals(channelType)){
                setTitle("微信收款");
                utils.sendTextMessage("请出示微信付款码","");
                mBinding.ivScanExample.setVisibility(View.VISIBLE);
                mBinding.ivScanExample.setImageResource(R.mipmap.wechat_scan_sample);
            }
        }
        ChargeInfo chargeInfo= (ChargeInfo) intent.getSerializableExtra("chargeModel");
        mViewModel = new SandPayCodeModel(this,mBinding,chargeInfo);
        mBinding.setViewModel(mViewModel);
        final FrameLayout view= (FrameLayout) findViewById(R.id.fl_scan_frame);
        final ImageView imageView= (ImageView) findViewById(R.id.iv_scan_needle);

        ViewTreeObserver vto2 = view.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                animator = ObjectAnimator.ofFloat(imageView, "translationY", view.getHeight());
                animator.setRepeatCount(ObjectAnimator.INFINITE);
                animator.setDuration(2000);
                animator.start();
            }
        });


        listenEditText();
    }
    private void listenEditText(){
        et_saomiaoqiang= (EditText) findViewById(R.id.et_auth_code);
        et_saomiaoqiang.setFocusable(true);
        et_saomiaoqiang.setFocusableInTouchMode(true);
        et_saomiaoqiang.requestFocus();
        ScannerGunWatcher watcher=new ScannerGunWatcher(et_saomiaoqiang);
        watcher.setOnWatcherListener(new ScannerGunWatcher.ScannerGunCallBack() {
            @Override
            public void callBack(String string) {
                if (mViewModel != null) {
                    mViewModel.pay(string);
                    animator.pause();
                }
            }
        });
        et_saomiaoqiang.addTextChangedListener(watcher);
    }

    @Override
    protected void onPause() {
        super.onPause();
        utils.onPause();
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
