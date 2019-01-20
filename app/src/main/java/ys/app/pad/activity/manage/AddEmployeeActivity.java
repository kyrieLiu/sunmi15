package ys.app.pad.activity.manage;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.databinding.ActivityAddEmployeeBinding;
import ys.app.pad.viewmodel.manage.AddEmployeeViewModel;

/**
 * Created by aaa on 2017/6/5.
 */

public class AddEmployeeActivity extends BaseActivity {

    private ActivityAddEmployeeBinding binding;
    private AddEmployeeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_employee);
        setBackVisiable();
        setTitle("新增员工");

        mViewModel = new AddEmployeeViewModel(this, binding);
        binding.setViewModel(mViewModel);
        setBgWhiteStatusBar();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mViewModel != null){
            mViewModel.reset();
        }
    }
}
