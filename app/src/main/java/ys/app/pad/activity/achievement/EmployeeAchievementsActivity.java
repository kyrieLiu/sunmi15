package ys.app.pad.activity.achievement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.activity.AchievementStatisticActivity;
import ys.app.pad.viewmodel.achievement.EmployeeAchievementsViewModel;
import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;


/**
 * Created by aaa on 2017/7/20.
 */

public class EmployeeAchievementsActivity extends BaseActivity {

    private TextView mTvStartTime,mTvEndTime,mTvRight;
    private IRecyclerView recyclerView;
    private EmployeeAchievementsViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_achievements);
        mTvStartTime= (TextView) findViewById(R.id.tv_achievement_start_time);
        mTvEndTime= (TextView) findViewById(R.id.tv_achievement_end_time);
        recyclerView= (IRecyclerView) findViewById(R.id.rv_achievement_detail);
        setBackVisiable();
        setTitle("员工绩效");
        mTvRight= (TextView) findViewById(R.id.search_tv);
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText("业绩统计");
        mTvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EmployeeAchievementsActivity.this,AchievementStatisticActivity.class);
                startActivity(intent);
            }
        });
        model = new EmployeeAchievementsViewModel(this,mTvStartTime,mTvEndTime,recyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        model.onDestory();
    }
}
