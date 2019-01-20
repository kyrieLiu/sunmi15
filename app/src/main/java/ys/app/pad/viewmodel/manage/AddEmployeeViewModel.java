package ys.app.pad.viewmodel.manage;

import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.AddEmployeeActivity;
import ys.app.pad.adapter.AddEmployeeAdapter;
import ys.app.pad.databinding.ActivityAddEmployeeBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AddEmployeeInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.WorkTypeInfo;
import ys.app.pad.widget.FullyLinearLayoutManager;

/**
 * Created by aaa on 2017/6/5.
 */

public class AddEmployeeViewModel extends BaseActivityViewModel{

    private AddEmployeeActivity mActivity;
    private ActivityAddEmployeeBinding mBinding;
    private AddEmployeeAdapter adapter;
    private List<AddEmployeeInfo> mList = new ArrayList<>();
    private int adapterPosition = 1;
    private Handler mUiHandler = new Handler();
    private ApiClient<BaseListResult<WorkTypeInfo>> mWorkTypeApiClient;
    private ApiClient<BaseResult> mApiClient;
    private RxManager mRxManager;


    public AddEmployeeViewModel(AddEmployeeActivity activity, ActivityAddEmployeeBinding binding) {
        this.mActivity = activity;
        this.mWorkTypeApiClient = new ApiClient<>();
        this.mBinding = binding;
        mApiClient = new ApiClient<>();
        mRxManager = new RxManager();
        init();
    }

    private void init() {
        mBinding.scrollView.setNestedScrollingEnabled(false);
        mList.add(new AddEmployeeInfo(adapterPosition));
        adapter = new AddEmployeeAdapter(mActivity, mList);
        mBinding.recyclerView.setLayoutManager(new FullyLinearLayoutManager(mActivity));
        loadHttp();
    }

    private void loadHttp() {

        mWorkTypeApiClient.reqApi("queryWorkType",new HashMap<String, String>(), ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<WorkTypeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<WorkTypeInfo> workTypeInfoBaseListResult) {
                        if (workTypeInfoBaseListResult.isSuccess()) {
                            adapter.setWorkType(workTypeInfoBaseListResult);
                        }else {
                            showToast(mActivity,workTypeInfoBaseListResult.getErrorMessage());
                        }
                        mBinding.recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mBinding.recyclerView.setAdapter(adapter);
                    }
                });
    }


    public void clickAddButton(View v) {
        adapterPosition++;
        mList.add(new AddEmployeeInfo(adapterPosition));
        adapter.notifyItemInserted(mList.size());
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                mBinding.scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });
    }

    public void clickButton(View v) {
        if (mList != null && !mList.isEmpty()){
            boolean isEmpty = false;
            for (AddEmployeeInfo info : mList){
                if (info.getGender() == 0 || TextUtils.isEmpty(info.getName())
                        || TextUtils.isEmpty(info.getNum()) || TextUtils.isEmpty(info.getPhone())
                        || TextUtils.isEmpty(info.getWorkType())){
                    isEmpty = true;
                    break;
                }
            }
            if (isEmpty){
                mActivity.showToast("请填写完整");
            }else {
                final Map<String,String> map = new HashMap<>();
                map.put("userList",new Gson().toJson(mList));
                map.put("shopId", SpUtil.getShopId()+"");
                map.put("branchId",SpUtil.getBranchId()+"");
                map.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
                mActivity.showRDialog();
                mApiClient.reqApi("insertUserList",map, ApiRequest.RespDataType.RESPONSE_JSON)
                        .updateUI(new Callback<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult baseResult) {
                                mActivity.hideRDialog();
                                if (baseResult.isSuccess()){
                                    mRxManager.post(Constants.type_add_employee,null);
                                    mActivity.finish();
                                }else {
                                    mActivity.showToast(baseResult.getErrorMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mActivity.hideRDialog();
                            }
                        });
            }
        }else {
            mActivity.showToast("请填写完整");
        }
    }

    public void reset() {
        if (mApiClient != null){
            mApiClient.reset();
            mApiClient = null;
        }
        if (mWorkTypeApiClient != null){
            mWorkTypeApiClient.reset();
            mWorkTypeApiClient = null;
        }
        if (mRxManager != null){
            mRxManager.clear();
            mRxManager = null;
        }
        mActivity = null;
    }
}
