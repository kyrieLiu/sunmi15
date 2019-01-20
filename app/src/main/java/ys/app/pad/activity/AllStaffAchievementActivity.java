package ys.app.pad.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.AllStaffAchievementActivityBinding;
import ys.app.pad.viewmodel.AllStaffAchievementModel;

public class AllStaffAchievementActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllStaffAchievementActivityBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_all_staff_achievement);
        setBackVisiable();
        TextView tv_title= (TextView) findViewById(R.id.title_tv);
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText("所有员工");
        AllStaffAchievementModel model=new AllStaffAchievementModel(this,binding.recyclerView);
        binding.setModel(model);
        model.init();



    }
}
