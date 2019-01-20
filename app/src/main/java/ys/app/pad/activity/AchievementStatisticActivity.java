package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.AchievementStatisticBinding;
import ys.app.pad.viewmodel.AchievementStatisticViewModel;

public class AchievementStatisticActivity extends BaseActivity {
    public static final int COMMODITY = 1;//商品
    public static final int SERVICE = 2;//服务
    public static final int RECHARGE = 3;//充值
    AchievementStatisticBinding binding;
    AchievementStatisticViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_achievement_statistic);
        setBackVisiable();
        viewModel = new AchievementStatisticViewModel(this,binding);
        binding.setViewModel(viewModel);
    }
}
