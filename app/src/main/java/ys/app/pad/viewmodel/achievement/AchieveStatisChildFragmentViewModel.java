package ys.app.pad.viewmodel.achievement;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseFragmentViewModel;
import ys.app.pad.Constants;
import ys.app.pad.adapter.AchieveStatisFragmentChildAdapter;
import ys.app.pad.databinding.AchievementStatisticChildBinding;
import ys.app.pad.fragment.AchievementStatisticChildFragment;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AchieveStatisChildInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.refresh.NestedRefreshLayout;
import ys.app.pad.widget.timeselector.TimeSelector;


/**
 * Created by Administrator on 2017/11/10/010.
 */

public class AchieveStatisChildFragmentViewModel extends BaseFragmentViewModel {
    private AchievementStatisticChildFragment mItemFragment;
    private NestedRefreshLayout mNestedRefreshLayout;
    public AchieveStatisFragmentChildAdapter adapter;
    private ApiClient<BaseListResult<AchieveStatisChildInfo>> apiClient;

    public ObservableField<String> obBeginDate = new ObservableField<>(DateUtil.getThisMonthFirstDayYMD());
    public ObservableField<String> obEndDate = new ObservableField<>(DateUtil.getTodayYMD());
    private int userId;//类型id
    private int type;//商品：1；服务：2
    private AchievementStatisticChildBinding binding;

    public AchieveStatisChildFragmentViewModel(AchievementStatisticChildFragment itemFragment, AchievementStatisticChildBinding binding) {
        this.mItemFragment = itemFragment;
        this.mNestedRefreshLayout = binding.nestedRefreshLayout;
        this.apiClient = new ApiClient<>();
        this.binding=binding;
        init();
        loadHttpData();
    }

    private void init() {
        Bundle bundle = mItemFragment.getArguments();
        userId = bundle.getInt(Constants.fragment_args);
        type = bundle.getInt(Constants.TYPE);

        adapter = new AchieveStatisFragmentChildAdapter(mItemFragment.getActivity(),false);
        LinearLayoutManager manager=new LinearLayoutManager(mItemFragment.getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recyclerView.setLayoutManager(manager);
        binding.recyclerView.setAdapter(adapter);
        mNestedRefreshLayout.setOnRefreshListener(new NestedRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startHttp = 0;
                loadHttpData();
            }
        });
    }

    public void onClickNetWorkError() {
        isNetWorkErrorVisible.set(false);
//        isLoadingVisible.set(true);
        mNestedRefreshLayout.froceRefreshToState(true);
        loadHttpData();
    }

    public void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mItemFragment.getActivity())) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataHttp();
        } else {//无网显示断网连接
            refreshNetUnavailable(mNestedRefreshLayout);
        }

    }

    private void getDataHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("userId", userId+"");
        params.put("type", type+"");
        params.put("beginTime", obBeginDate.get());
        params.put("endTime", obEndDate.get());
        apiClient.reqApi("selectUserPerformanceByType",params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AchieveStatisChildInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AchieveStatisChildInfo> result) {
                        if (result.isSuccess()){
                            mNestedRefreshLayout.refreshFinish();
                            List<AchieveStatisChildInfo> list = result.getData();
                            if (null != list && list.size() > 0){
                                adapter.setList(list);
                                loadSecMoreSuccess(adapter,list);
                            }else {
                                refreshSuccess(list);
                                loadSecMoreSuccess(adapter,list);
                            }
                        }else {
                            refreshFailed(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        refreshFailed(e.getMessage());
                    }
                });
    }

    public void clickBeginDate(View view) {
        TimeSelector timeSelector = new TimeSelector(mItemFragment.getActivity(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obBeginDate.set(DateUtil.longFormatDate(time));
                startHttp = 0;
                mNestedRefreshLayout.froceRefreshToState(true);
                loadHttpData();
            }
        }, "2017-01-01 00:00:00", DateUtil.longFormatDate2(System.currentTimeMillis()));
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }

    public void clickEndDate(View view) {
        TimeSelector timeSelector = new TimeSelector(mItemFragment.getActivity(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                obEndDate.set(DateUtil.longFormatDate(time));
                startHttp = 0;
                mNestedRefreshLayout.froceRefreshToState(true);
                loadHttpData();
            }
        }, "2017-01-01 00:00:00", DateUtil.longFormatDate2(System.currentTimeMillis()));
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }
}
