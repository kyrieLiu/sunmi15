package ys.app.pad.viewmodel;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.AllStaffAchievementActivity;
import ys.app.pad.adapter.manage.EmployeeNameNewAdapter;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.utils.NetWorkUtil;

/**
 * Created by liuyin on 2017/7/31.
 */

public class AllStaffAchievementModel extends BaseActivityViewModel {

    private RecyclerView recyclerView;
    private EmployeeNameNewAdapter mAdapter;
    private AllStaffAchievementActivity mActivity;
    private final ApiClient<BaseListResult<EmployeeInfo>> mClient;

    public AllStaffAchievementModel(AllStaffAchievementActivity mActivity,RecyclerView recyclerView) {
        this.recyclerView=recyclerView;
        this.mActivity=mActivity;
        this.mClient = new ApiClient<BaseListResult<EmployeeInfo>>();
    }
    public void init(){
        mAdapter = new EmployeeNameNewAdapter(mActivity);
        recyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
        recyclerView.setAdapter(mAdapter);
        loadHttpData();
    }
    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            isNetWorkErrorVisible.set(true);
        }
    }
    private void getDataList() {
        isLoadingVisible.set(true);
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        mClient.reqApi("queryEmployeeList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            setData(result.getData());
                        } else {
                            isNetWorkErrorVisible.set(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoadingVisible.set(false);
                        isNetWorkErrorVisible.set(true);
                        super.onError(e);
                    }
                });
    }
    private void setData(List<EmployeeInfo> data) {
        mAdapter.setList(data);
    }
}
