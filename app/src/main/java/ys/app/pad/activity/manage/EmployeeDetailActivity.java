package ys.app.pad.activity.manage;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityEmployeeDetailBinding;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.viewmodel.manage.EmployeeDetailViewModel;

/**
 * Created by lyy on 2017/6/26.
 */

public class EmployeeDetailActivity extends BaseActivity {

    private ActivityEmployeeDetailBinding binding;
    private EmployeeDetailViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee_detail);
        setBackVisiable();
        setTitle("员工详情");
        final EmployeeInfo info = (EmployeeInfo) getIntent().getSerializableExtra(Constants.intent_info);
        TextView editTv = (TextView)findViewById(R.id.edit_tv);
        editTv.setVisibility(View.VISIBLE);
        editTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeDetailActivity.this,EditEmployeeActivity.class);
                intent.putExtra(Constants.intent_info,info);
                startActivity(intent);
                finish();
            }
        });
        mViewModel = new EmployeeDetailViewModel(this,binding);
        binding.setViewModel(mViewModel);
        mViewModel.setInfoFormIntent(info);
    }
}
