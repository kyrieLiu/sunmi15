package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAboutUsBinding;
import ys.app.pad.viewmodel.manage.AboutUsViewModel;

/**
 * Created by lyy on 2017/7/3.
 */

public class AboutUsActivity extends BaseActivity {

    private ActivityAboutUsBinding binding;
    private AboutUsViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);
        setBackVisiable();
        setTitle("功能介绍");
        mViewModel = new AboutUsViewModel(this);
        binding.setViewModel(mViewModel);
        mViewModel.getVersionName();
        setBgWhiteStatusBar();
    }
}
