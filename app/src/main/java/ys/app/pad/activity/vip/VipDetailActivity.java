package ys.app.pad.activity.vip;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityVipDetailBinding;
import ys.app.pad.model.VipInfo;
import ys.app.pad.viewmodel.vip.VipDetailViewModel;

/**
 * Created by aaa on 2017/3/1.
 */

public class VipDetailActivity extends BaseActivity {


    private ActivityVipDetailBinding binding;
    private VipDetailViewModel mViewModel;
    private VipInfo mInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vip_detail);
        setBackVisiable();
        setTitle("会员详情");

        int flag=getIntent().getIntExtra(Constants.intent_type,0);//0代表会员卡,1代表次卡
        mViewModel = new VipDetailViewModel(this, binding,flag);
        binding.setViewModel(mViewModel);
        mViewModel.init();
        mInfo = (VipInfo) getIntent().getSerializableExtra(Constants.intent_info);
        mViewModel.setData(mInfo);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
}
