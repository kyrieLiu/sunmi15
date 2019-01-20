package ys.app.pad.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ServiceDetailActivityBinding;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.viewmodel.ServiceDetailViewModel;

/**
 * Created by aaa on 2017/5/31.
 */

public class ServiceDetailActivity extends BaseActivity {
    private ServiceDetailActivityBinding binding;
    private ServiceDetailViewModel mViewModel;
    private ServiceInfo result;
    private String mFlag;
    private TextView mModifyTv;
    private String mSale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_service_detail);
        setBackVisiable();

        mModifyTv = (TextView)findViewById(R.id.modify_tv);
        mModifyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceDetailActivity.this,ModifyServiceDetailActivity.class);
                intent.putExtra(Constants.intent_service_info,result);
                startActivity(intent);
            }
        });
        getDataFromIntent();
        setBgWhiteStatusBar();

    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        result = (ServiceInfo) extras.getSerializable(Constants.intent_service_info);
        int position =  extras.getInt(Constants.intent_service_position);
        boolean aBoolean = extras.getBoolean(Constants.intent_promotion);
        mViewModel = new ServiceDetailViewModel(this,binding,result,position,aBoolean);
        binding.setViewModel(mViewModel);
        mViewModel.init();
        if (aBoolean){
            setTitle("加入促销");
             mModifyTv.setVisibility(View.GONE);
        }else {
            setTitle("服务详情");
            mModifyTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.reset();
    }
}
