package ys.app.pad.viewmodel.manage;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.greendao.EmployeeInfoDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.EmployeeDetailActivity;
import ys.app.pad.activity.manage.EmployeeListActivity;
import ys.app.pad.adapter.manage.EmployeeListAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ActivityEmployeeListBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

/**
 * Created by aaa on 2017/6/5.
 */

public class EmployeeListViewModel extends BaseActivityViewModel {

    private final EmployeeListActivity mActivity;
    private final ActivityEmployeeListBinding mBinding;
    private final ApiClient<BaseListResult<EmployeeInfo>> mListClient;
    private LoadMoreFooterView mLoadMoreFooterView;
    private EmployeeListAdapter mAdapter;
    private RxManager mRxManager;
    private CustomDialog mDialog;
    private ApiClient<BaseResult> mApiClient;

    public EmployeeListViewModel(EmployeeListActivity activity, ActivityEmployeeListBinding mBinding) {
        this.mActivity = activity;
        this.mBinding = mBinding;
        this.mListClient = new ApiClient<BaseListResult<EmployeeInfo>>();
        this.mApiClient = new ApiClient<BaseResult>();
        mRxManager = new RxManager();
    }

    @Override
    public void reloadData(View view) {
        loadHttpData();
    }


    public void init() {

//        mLoadMoreFooterView = (LoadMoreFooterView) mBinding.recyclerView.getLoadMoreFooterView();

        mBinding.recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
//                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
//                startHttp = 0;
                loadHttpData();
            }
        });


        mAdapter = new EmployeeListAdapter(mActivity);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
        mBinding.recyclerView.setIAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity, EmployeeDetailActivity.class);
                intent.putExtra(Constants.intent_info, mAdapter.getList().get(position));
                mActivity.startActivity(intent);
            }
        });

        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                showDeleteDialog(position);
            }
        });

        loadHttpData();
        mRxManager.on(Constants.type_add_employee, new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData();
            }
        });
        mRxManager.on(Constants.bus_type, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (Constants.bus_type_update_employee == integer) {
                    loadHttpData();
                }
            }
        });
    }

    private void showDeleteDialog(final int position) {
        if (mDialog == null) {
            mDialog = new CustomDialog(mActivity);
            mDialog.setContent("确定删除该员工?");
            mDialog.setCancelVisiable();
        }

        mDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showRDialog();
                Map<String, String> params = new HashMap<>();
                params.put("id", mAdapter.getList().get(position).getId() + "");
                params.put("shopId", SpUtil.getShopId() + "");
                params.put("branchId",SpUtil.getBranchId()+"");
                params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
                params.put("status", "4");
                mApiClient.reqApi("updateEmployee", params, ApiRequest.RespDataType.RESPONSE_JSON)
                        .updateUI(new Callback<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult baseResult) {
                                mActivity.hideRDialog();
                                mDialog.dismiss();
                                if (baseResult.isSuccess()) {
                                    mAdapter.removeData(position);
                                } else {
                                    mActivity.showToast(baseResult.getErrorMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mDialog.dismiss();
                                mActivity.hideRDialog();
                                mActivity.showToast(e.getMessage());
                            }
                        });

            }
        });

        mDialog.show();
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            afterRefreshFailed(mBinding.recyclerView);
        }
    }

    private void getDataList() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("limit", "1000");

        mListClient.reqApi("queryEmployeeList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> employeeInfoBaseListResult) {
                        if (employeeInfoBaseListResult.isSuccess()) {
                            EmployeeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao();
                            List<EmployeeInfo> list = employeeInfoBaseListResult.getData();
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }
                })
                .updateUI(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> result) {
                        if (result.isSuccess()) {
                            List<EmployeeInfo> data = result.getData();
                            mAdapter.setList(startHttp, data);
                            if (data == null) {
                                afterRefreshFailed(mBinding.recyclerView);
                            } else {
                                afterRefreshSuccess(mBinding.recyclerView, data);
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                            afterRefreshFailed(mBinding.recyclerView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        afterRefreshFailed(mBinding.recyclerView);
                    }
                });


    }

}

