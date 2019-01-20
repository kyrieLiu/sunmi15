package ys.app.pad.viewmodel.manage;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.EditEmployeeActivity;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.adapter.SelectWorkTypeAdapter;
import ys.app.pad.databinding.ActivityEditEmployeeBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.WorkTypeInfo;
import ys.app.pad.widget.dialog.SelectDialog;

/**
 * Created by lyy on 2017/6/27.
 */

public class EditEmployeeViewModel {
    private final EditEmployeeActivity mActivity;
    private final ActivityEditEmployeeBinding mBinding;
    private final ApiClient<BaseResult> mCommitClient;
    private EmployeeInfo mInfo;
    private SelectDialog mSelectDialog;
    public ObservableField<String> obGender = new ObservableField<>();
    public ObservableField<String> obWorkType = new ObservableField<>();
    public ObservableField<String> obName = new ObservableField<>();
    public ObservableField<String> obPhone = new ObservableField<>();
    public ObservableField<String> obNum = new ObservableField<>();
    public ObservableInt obGenderType = new ObservableInt();
    public ObservableInt obWorkTypeId = new ObservableInt();
    private ApiClient<BaseListResult<WorkTypeInfo>> mWorkTypeApiClient;
    private List<WorkTypeInfo> mWorkTypeInfos = new ArrayList<>();
    private SelectDialog mWorkTypeSelectDialog;


    public EditEmployeeViewModel(EditEmployeeActivity activity, ActivityEditEmployeeBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mWorkTypeApiClient = new ApiClient();
        this.mCommitClient = new ApiClient<BaseResult>();
        getWorkTypeHttp();
    }

    private void getWorkTypeHttp() {

        mWorkTypeApiClient.reqApi("queryWorkType", new HashMap<String, String>(), ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<WorkTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<WorkTypeInfo> workTypeInfoBaseListResult) {
                        if (workTypeInfoBaseListResult.isSuccess()) {
                            mWorkTypeInfos = workTypeInfoBaseListResult.getData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    public void setInfoFormIntent(EmployeeInfo info) {
        this.mInfo = info;
        obGender.set(mInfo.getSex() == 1 ? "女" : "男");
        obGenderType.set(mInfo.getSex());
        obName.set(mInfo.getName());
        obWorkType.set(mInfo.getPost());
        obPhone.set(mInfo.getPhone());
        obNum.set(mInfo.getJobNumber());
    }

    public void clickGenderButton(View v) {
        if (mSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("男", "2"));
            genderInfos.add(new SelectInfo("女", "1"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    obGenderType.set(Integer.parseInt(genderInfo.getType()));
                    obGender.set(genderInfo.getName());
                }
            });
        }
        mSelectDialog.show();
    }

    public void selectTypeWork() {
        if (mWorkTypeInfos.isEmpty()) return;
        if (mWorkTypeSelectDialog == null) {
            SelectWorkTypeAdapter adapter = new SelectWorkTypeAdapter(mActivity, mWorkTypeInfos);
            mWorkTypeSelectDialog = new SelectDialog(mActivity, adapter);
            mWorkTypeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    WorkTypeInfo genderInfo = mWorkTypeInfos.get(position);
                    obWorkTypeId.set(genderInfo.getId());
                    obWorkType.set(genderInfo.getName());
                }
            });
        }
        mWorkTypeSelectDialog.show();
    }

    public void clickOkBtn(View v){

        Map<String,String> params  = new HashMap<>();
        params.put("id",mInfo.getId()+"");
        params.put("shopId", SpUtil.getShopId()+"");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("name",obName.get());
        params.put("sex",obGenderType.get()+"");
        params.put("post",obWorkType.get());
        params.put("phone",obPhone.get());
        params.put("jobNumber",obNum.get());
        mActivity.showRDialog();
        mCommitClient.reqApi("updateEmployee", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            RxManager rxManager = new RxManager();
                            rxManager.post(Constants.bus_type, Constants.bus_type_update_employee);
                            mActivity.showToast("修改成功");
                            mActivity.finish();
                        }else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });
    }
}
