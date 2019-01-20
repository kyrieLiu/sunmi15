package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAddVipCardBinding;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.viewmodel.manage.AddVipCardViewModel;

/**
 * Created by aaa on 2017/3/17.
 */

public class AddVipCardActivity extends BaseActivity {

    private ActivityAddVipCardBinding binding;
    private AddVipCardViewModel mViewModel;
    private int classification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vip_card);
        setBackVisiable();
        classification = getIntent().getIntExtra(Constants.intent_flag,0);
        mViewModel = new AddVipCardViewModel(this,binding,classification);
        binding.setViewModel(mViewModel);
        getDataFromIntent();
        mViewModel.init();
        setBgWhiteStatusBar();

    }

    private void getDataFromIntent() {
        VipCardInfo info = (VipCardInfo)getIntent().getSerializableExtra(Constants.intent_info);

        if(info!=null){
            mViewModel.setIntentData(info);
            if (classification==0){
                setTitle("编辑会员卡");
                Button blue_save_btn = (Button) findViewById(R.id.blue_save_btn);
                blue_save_btn.setVisibility(View.VISIBLE);
                blue_save_btn.setEnabled(mViewModel.obButtonEnable.get());
                blue_save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewModel.clickButton(v);
                    }
                });
            } else if (classification==2)setTitle("编辑折扣卡");
            else setTitle("编辑生日折扣卡");
            //setTitle("编辑会员卡");
        }else {
            if (classification==0)setTitle("添加会员卡");
            else if (classification==2)setTitle("添加折扣卡");
            else setTitle("添加生日折扣卡");
            Button blue_save_btn = (Button) findViewById(R.id.blue_save_btn);
            blue_save_btn.setVisibility(View.VISIBLE);
            blue_save_btn.setText("保存");
            blue_save_btn.setEnabled(mViewModel.obButtonEnable.get());
            blue_save_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModel.clickButton(v);
                }
            });
            //setTitle("添加会员卡");
        }
    }

}
