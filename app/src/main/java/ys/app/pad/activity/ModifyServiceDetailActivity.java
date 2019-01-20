package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ModifyServiceDetailActivityBinding;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.viewmodel.ModifyServiceDetailViewModel;

/**
 * Created by Administrator on 2017/6/9.
 */

public class ModifyServiceDetailActivity extends BaseActivity {

    private ModifyServiceDetailActivityBinding binding;
    private ModifyServiceDetailViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_service);
        setBackVisiable();
        setTitle("修改服务");
        getDataFormIntent();
        setBgWhiteStatusBar();

    }

    private void getDataFormIntent() {
        ServiceInfo result = (ServiceInfo)getIntent().getSerializableExtra(Constants.intent_service_info);
        mViewModel = new ModifyServiceDetailViewModel(this,binding,result);
        binding.setViewModel(mViewModel);
        mViewModel.getTypeHttp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onRest();
    }
}
