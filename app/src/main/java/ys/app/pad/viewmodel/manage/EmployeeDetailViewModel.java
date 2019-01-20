package ys.app.pad.viewmodel.manage;

import ys.app.pad.activity.manage.EmployeeDetailActivity;
import ys.app.pad.databinding.ActivityEmployeeDetailBinding;
import ys.app.pad.model.EmployeeInfo;

/**
 * Created by lyy on 2017/6/26.
 */

public class EmployeeDetailViewModel {
    private final EmployeeDetailActivity mActivity;
    private final ActivityEmployeeDetailBinding mBinding;
    public EmployeeInfo mInfo;

    public EmployeeDetailViewModel(EmployeeDetailActivity activity, ActivityEmployeeDetailBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
    }

    public void setInfoFormIntent(EmployeeInfo info) {
        this.mInfo = info;
    }
}
