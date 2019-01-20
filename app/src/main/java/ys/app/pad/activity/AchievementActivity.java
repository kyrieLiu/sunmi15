package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAchievementBinding;
import ys.app.pad.viewmodel.AchievementViewModel;

/**
 * Created by aaa on 2017/6/7.
 */

public class AchievementActivity extends BaseActivity {

    private ActivityAchievementBinding binding;
    private AchievementViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_achievement);
        setBackVisiable();
        setTitle("业绩");
        TextView tongjiTv = (TextView) findViewById(R.id.tongji_tv);
        tongjiTv.setVisibility(View.VISIBLE);
        tongjiTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToActivity(StatisticsActivity.class);
            }
        });
        mViewModel = new AchievementViewModel(this, binding);
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
