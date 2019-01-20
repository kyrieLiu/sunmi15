package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityModifyBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.ModifyViewModel;

/**
 * Created by aaa on 2017/3/8.
 */

public class ModifyActivity extends BaseActivity {

    private ActivityModifyBinding binding;
    private ModifyViewModel mViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify);
        setBackVisiable();
        setTitle("修改商品");
        getDataFormIntent();
        setBgWhiteStatusBar();

    }

    private void getDataFormIntent() {
        GoodInfo result = (GoodInfo)getIntent().getSerializableExtra(Constants.intent_info);
        mViewModel = new ModifyViewModel(this,binding,result);
        binding.setViewModel(mViewModel);
        mViewModel.getTypeHttp();
        mViewModel.getUnitHttp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.onRest();
    }
}
