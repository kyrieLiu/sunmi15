package ys.app.pad.activity.manage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityVipCardBinding;
import ys.app.pad.viewmodel.manage.VipCardViewModel;

/**
 * Created by aaa on 2017/3/16.
 */

public class VipCardActivity extends BaseActivity {
    
    private ActivityVipCardBinding binding;
    private VipCardViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vip_card);
        setBackVisiable();
        final int classification=getIntent().getIntExtra(Constants.intent_flag,0);
        if (classification==0)setTitle("会员卡");
        else if (classification==2)setTitle("折扣卡");
        else setTitle("生日折扣卡");
        ImageView addTv = (ImageView) findViewById(R.id.add_iv);
        addTv.setVisibility(View.VISIBLE);
        addTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VipCardActivity.this,AddVipCardActivity.class);
                intent.putExtra(Constants.intent_flag,classification);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
            }
        });

        mViewModel = new VipCardViewModel(this,binding,classification);
        binding.setViewModel(mViewModel);
        mViewModel.init();
        setBgWhiteStatusBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
}
