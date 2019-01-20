package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityEmployeeListBinding;
import ys.app.pad.viewmodel.manage.EmployeeListViewModel;

/**
 * Created by aaa on 2017/6/5.
 */

public class EmployeeListActivity extends BaseActivity {

    private ActivityEmployeeListBinding binding;
    private EmployeeListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_list);
        setBackVisiable();
        setTitle("员工");
        ImageView mAddTv = (ImageView) findViewById(R.id.add_iv);
        mAddTv.setVisibility(View.VISIBLE);
        mAddTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnToActivity(AddEmployeeActivity.class);
            }
        });


        mViewModel = new EmployeeListViewModel(this, binding);
        binding.setViewModel(mViewModel);
        mViewModel.init();
        setBgWhiteStatusBar();


    }
}
