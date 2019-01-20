package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityLoginBinding;
import ys.app.pad.viewmodel.LoginViewModel;

/**
 * Created by lyy on 2017/2/20 10:06.
 * email：2898049851@qq.com
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        mViewModel = new LoginViewModel(this, binding);
        binding.setViewModel(mViewModel);


    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.destroy();
    }
}
