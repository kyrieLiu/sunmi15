package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityEditEmployeeBinding;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.viewmodel.manage.EditEmployeeViewModel;

/**
 * Created by lyy on 2017/6/27.
 */

public class EditEmployeeActivity extends BaseActivity {
    private ActivityEditEmployeeBinding binding;
    private EditEmployeeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_employee);
        setBackVisiable();
        setTitle("员工详情");
        final EmployeeInfo info = (EmployeeInfo) getIntent().getSerializableExtra(Constants.intent_info);
        mViewModel = new EditEmployeeViewModel(this,binding);
        binding.setViewModel(mViewModel);
        mViewModel.setInfoFormIntent(info);
    }
}
