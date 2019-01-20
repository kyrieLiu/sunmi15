package ys.app.pad.viewmodel;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.PayListActivity;
import ys.app.pad.adapter.Order2Adapter;
import ys.app.pad.databinding.ActivityPayListBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

/**
 * Created by aaa on 2017/4/25.
 */

public class PayListViewModel extends BaseActivityViewModel {

    private final PayListActivity mActivity;
    private final ActivityPayListBinding mBinding;
    private final ApiClient<BaseListResult<OrderInfo>> mListClient;
    private LoadMoreFooterView loadMoreFooterView;
    //    private PayListAdapter mAdapter;
    private Order2Adapter mAdapter;
    private RxManager mRxManager;

    public PayListViewModel(PayListActivity activity, ActivityPayListBinding binding) {
        this.mBinding = binding;
        this.mActivity = activity;
        this.mListClient = new ApiClient<BaseListResult<OrderInfo>>();

        init();
    }

    @Override
    public void reloadData(View view) {
        loadHttpData();
    }

    public void init() {
        mRxManager = new RxManager();
        mRxManager.on(Constants.bus_type_delete_position, new Action1<Integer>() {
            @Override
            public void call(Integer registerI) {
                mAdapter.removeData(registerI);
            }
        });

        firstCome = true;
        startHttp = 0;
        mBinding.recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData();
            }
        });

        loadMoreFooterView = (LoadMoreFooterView) mBinding.recyclerView.getLoadMoreFooterView();

        mAdapter = new Order2Adapter(mActivity);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.recyclerView.setIAdapter(mAdapter);
        mBinding.recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMoreFooterView.canLoadMore() && mAdapter.getItemCount() > 0) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
        loadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            @Override
            public void onRetry(LoadMoreFooterView view) {
                if (loadMoreFooterView.canLoadMore() && mAdapter.getItemCount() > 0) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });


        loadHttpData();
        mBinding.recyclerView.getItemAnimator().setAddDuration(100);
        mBinding.recyclerView.getItemAnimator().setRemoveDuration(100);
        mBinding.recyclerView.getItemAnimator().setMoveDuration(200);
        mBinding.recyclerView.getItemAnimator().setChangeDuration(100);

    }


    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            afterRefreshAndLoadMoreFailed(mBinding.recyclerView, loadMoreFooterView);
        }
    }


    public void getDataList() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("type", "0");
        params.put("orderInfo", "2");
        params.put("start", startHttp + "");
        params.put("beginTime", DateUtil.longFormatDate(System.currentTimeMillis() - 1000*60*60*24*2));
        mListClient.reqApi("queryOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<OrderInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<OrderInfo> result) {
                        if (result.isSuccess()) {
                            List<OrderInfo> data = result.getData();
                            mAdapter.setList(startHttp, data);
                            afterRefreshAndLoadMoreSuccess(data, mBinding.recyclerView, loadMoreFooterView);

                        } else {
                            mActivity.showToast(result.getErrorMessage());
                            afterRefreshAndLoadMoreFailed(mBinding.recyclerView, loadMoreFooterView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        afterRefreshAndLoadMoreFailed(mBinding.recyclerView, loadMoreFooterView);
                    }
                });
    }
    public void clear(){
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
    }

}
