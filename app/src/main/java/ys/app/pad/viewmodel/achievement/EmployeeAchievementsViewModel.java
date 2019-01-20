package ys.app.pad.viewmodel.achievement;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.activity.achievement.EmployeeAchievementsActivity;
import ys.app.pad.adapter.achievement.EmployeeAchievementsAdapter;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.EmployeeAchievementsInfo;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

/**
 * Created by aaa on 2017/7/20.
 */

public class EmployeeAchievementsViewModel extends BaseActivityViewModel {
    private final EmployeeAchievementsActivity mActivity;
    public EmployeeAchievementsAdapter adapter;
    private ApiClient<BaseListResult<EmployeeAchievementsInfo>> apiClient;
    private TextView mTvStartTime, mTvEndTime;
    private IRecyclerView recyclerView;
    private LoadMoreFooterView mLoadMoreFooterView;
    private String startTime,endTime;


    public EmployeeAchievementsViewModel(EmployeeAchievementsActivity activity, TextView mTvStartTime, TextView mTvEndTime, IRecyclerView recyclerView) {
        this.mActivity = activity;
        this.apiClient = new ApiClient<>();
        this.mTvStartTime = mTvStartTime;
        this.mTvEndTime = mTvEndTime;
        this.recyclerView = recyclerView;
        startTime=DateUtil.getThisMonthFirstDayYMD();
        endTime=DateUtil.getTodayYMD();
        mTvStartTime.setText(startTime);
        mTvEndTime.setText(endTime);
        init();

    }


    private void init() {

        mLoadMoreFooterView = (LoadMoreFooterView) recyclerView.getLoadMoreFooterView();

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData(false);
            }
        });
        adapter = new EmployeeAchievementsAdapter(mActivity);
        LinearLayoutManager manager=new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setIAdapter(adapter);
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mLoadMoreFooterView.canLoadMore() && adapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData(false);
                }
            }
        });
        loadHttpData(true);

        mTvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBeginDate();
            }
        });
        mTvEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEndDate();
            }
        });
    }

    private void loadHttpData(boolean showDialog) {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataHttp(showDialog);
        } else {//无网显示断网连接
        }

    }

    private void getDataHttp(boolean showDialog) {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("beginTime", startTime);
        params.put("endTime", endTime);
        params.put("start", startHttp + "");
        params.put("limit", "10");
        if (showDialog){
            mActivity.showRDialog();
        }

        apiClient.reqApi("bossSelectUserList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<EmployeeAchievementsInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeAchievementsInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            List<EmployeeAchievementsInfo> dataList = result.getData();
                            if (dataList.size() > 0 && null != dataList) {
                                adapter.setList(startHttp, dataList);
                            } else {
                                adapter.setList(startHttp, dataList);
                            }
                            afterRefreshAndLoadMoreSuccess(dataList, recyclerView, mLoadMoreFooterView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        mActivity.showToast("网络异常");
                        afterRefreshAndLoadMoreFailed(recyclerView, mLoadMoreFooterView);
                    }
                });
    }

    public void clickBeginDate() {
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                startTime=DateUtil.longFormatDate(time);
                mTvStartTime.setText(startTime);
                startHttp = 0;
                loadHttpData(true);
            }
        }, "2017-01-01 00:00:00", DateUtil.longFormatDate2(System.currentTimeMillis()));
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }

    public void clickEndDate() {
        TimeSelector timeSelector = new TimeSelector(mActivity, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                endTime=DateUtil.longFormatDate(time);
                mTvEndTime.setText(endTime);
                startHttp = 0;
                loadHttpData(true);
            }
        }, "2017-01-01 00:00:00", DateUtil.longFormatDate2(System.currentTimeMillis()));
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }

    public void onDestory() {
        adapter = null;
        apiClient = null;
    }
}
