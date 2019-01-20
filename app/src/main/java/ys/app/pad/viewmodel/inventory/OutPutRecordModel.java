package ys.app.pad.viewmodel.inventory;

import android.databinding.ObservableField;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.adapter.inventory.OutPutInventoryAdapter;
import ys.app.pad.fragment.OutPutInventoryFragment;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.OutStorageInfo;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.timeselector.TimeSelector;
import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;


/**
 * Created by liuyin on 2017/11/10.
 */

public class OutPutRecordModel extends BaseActivityViewModel {
    private OutPutInventoryFragment fragment;
    private IRecyclerView recyclerView;
    private OutPutInventoryAdapter adapter;
    private ApiClient<BaseListResult<OutStorageInfo>> apiClient;
    private LoadMoreFooterView mLoadMoreFooterView;
    public ObservableField<String> obStartTime=new ObservableField();
    public ObservableField<String> obEndTime=new ObservableField();
    private int type;
    public OutPutRecordModel(OutPutInventoryFragment fragment, IRecyclerView recyclerView, int type){
        this.fragment=fragment;
        this.recyclerView=recyclerView;
        this.type=type;
        this.apiClient=new ApiClient<>();
    }
    public void initView(){
       initRecyclerView();
       SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
       obStartTime.set(format.format(new Date()));
       obEndTime.set(format.format(new Date()));
        loadHttpData();
    }
    private void initRecyclerView(){
        adapter=new OutPutInventoryAdapter(fragment,type);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getActivity()));
        recyclerView.setIAdapter(adapter);
        mLoadMoreFooterView = (LoadMoreFooterView) recyclerView.getLoadMoreFooterView();

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData();
            }
        });
        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mLoadMoreFooterView.canLoadMore() && adapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
        mLoadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            public void onRetry(LoadMoreFooterView view) {
                if (mLoadMoreFooterView.canLoadMore() && adapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(fragment.getActivity())) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataList();
        } else {//无网显示断网连接
            fragment.showToast(Constants.network_error_warn);
            afterRefreshAndLoadMoreFailed(recyclerView, mLoadMoreFooterView);
        }
    }
    private void getDataList(){
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("type", type + "");
        params.put("beginTime", obStartTime.get());
        params.put("endTime", obEndTime.get());
        params.put("start", startHttp + "");
        apiClient.reqApi("selectStockList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<OutStorageInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<OutStorageInfo> result) {
                        fragment.hideRDialog();
                        if (result.isSuccess()) {
                            List<OutStorageInfo> data = result.getData();
                            adapter.setList(startHttp, data);

                                afterRefreshAndLoadMoreSuccess(data, recyclerView, mLoadMoreFooterView);
                        } else {
                            Toast.makeText(fragment.getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        fragment.hideRDialog();
                        Toast.makeText(fragment.getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                        afterRefreshAndLoadMoreFailed(recyclerView, mLoadMoreFooterView);
                    }
                });
    }

    public void clickBeginDate(View view) {
        TimeSelector timeSelector = new TimeSelector(fragment.getActivity(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                String splitTime=time.substring(0,10);//只保留年月日
                obStartTime.set(splitTime);
                startHttp = 0;
                loadHttpData();
            }
        }, "2017-01-01 00:00:00", DateUtil.longFormatDate2(System.currentTimeMillis()));
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }

    public void clickEndDate(View view) {
        TimeSelector timeSelector = new TimeSelector(fragment.getActivity(), new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                String splitTime=time.substring(0,10);//只保留年月日
                obEndTime.set(splitTime);
                startHttp = 0;
                loadHttpData();
            }
        }, "2017-01-01 00:00:00", DateUtil.longFormatDate2(System.currentTimeMillis()));
        timeSelector.setMode(TimeSelector.MODE.YMD);//只显示 年月日
        timeSelector.show();
    }


    public void clear() {


    }
}
