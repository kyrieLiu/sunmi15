package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityStatisticsBinding;
import ys.app.pad.viewmodel.StatisticsViewModel;

/**
 * Created by aaa on 2017/6/7.
 */

public class StatisticsActivity extends BaseActivity {

    private ActivityStatisticsBinding binding;
    private StatisticsViewModel mViewModel;
    private TextView tv_print;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_statistics);
        setBackVisiable();
        setTitle("统计");
        mViewModel = new StatisticsViewModel(this, binding);
        binding.setViewModel(mViewModel);
        setBgWhiteStatusBar();

        tv_print = (TextView) findViewById(R.id.edit_tv);
        tv_print.setText("打印");
        tv_print.setVisibility(View.VISIBLE);
        tv_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.printOrder(tv_print);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
}
