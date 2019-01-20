package ys.app.pad.viewmodel.manage;

import android.support.v7.widget.GridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.manage.AllotRecordActivity;
import ys.app.pad.adapter.AllotRecordAdapter;
import ys.app.pad.databinding.AllotRecordAcitivtyBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AllotRecordInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;


/**
 * Created by liuyin on 2017/9/16.
 */

public class AllotRecordModel extends BaseActivityViewModel {

    private AllotRecordAcitivtyBinding mBinding;
    private AllotRecordAdapter mAdapter;
    private AllotRecordActivity mActivity;
    private LoadMoreFooterView mLoadMoreFooterView;
    private ApiClient<BaseListResult<AllotRecordInfo>> mListClient;

    public AllotRecordModel(AllotRecordAcitivtyBinding mBinding,AllotRecordActivity mActivity){
            this.mBinding=mBinding;
        this.mActivity= mActivity;
        this.mListClient = new ApiClient<BaseListResult<AllotRecordInfo>>();
    }
    public void initView(){
        List<AllotRecordInfo> list=new ArrayList<>();
        mLoadMoreFooterView = (LoadMoreFooterView) mBinding.recyclerView.getLoadMoreFooterView();
        mAdapter=new AllotRecordAdapter(list,mActivity);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity,3));
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData();
            }
        });
        mBinding.recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mLoadMoreFooterView.canLoadMore() && mAdapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
        mLoadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            @Override
            public void onRetry(LoadMoreFooterView view) {
                if (mLoadMoreFooterView.canLoadMore() && mAdapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
        loadHttpData();
    }
    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            afterRefreshAndLoadMoreFailed(mBinding.recyclerView, mLoadMoreFooterView);
        }
    }
    private void getDataList(){
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("start",startHttp+"");

        mListClient.reqApi("selectAllocationList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AllotRecordInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AllotRecordInfo> result) {
                        if (result.isSuccess()) {
                            List<AllotRecordInfo> data = result.getData();
                            mAdapter.setList(startHttp, data);
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerView, mLoadMoreFooterView);
                            } else {
                                afterRefreshAndLoadMoreSuccess(data, mBinding.recyclerView, mLoadMoreFooterView);
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                            afterRefreshAndLoadMoreFailed(mBinding.recyclerView, mLoadMoreFooterView);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        afterRefreshAndLoadMoreFailed(mBinding.recyclerView, mLoadMoreFooterView);
                    }
                });

    }
    public void reset(){
        if (mListClient!=null){
            mListClient.reset();
            mListClient=null;
        }
        mActivity = null;
    }
}
