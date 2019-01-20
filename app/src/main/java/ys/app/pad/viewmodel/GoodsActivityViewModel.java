package ys.app.pad.viewmodel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.greendao.GoodTypeInfoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.AppApplication;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.AddInventoryActivity;
import ys.app.pad.activity.GoodsActivity;
import ys.app.pad.activity.GoodsDetailActivity;
import ys.app.pad.activity.TakeOutInventoryActivity;
import ys.app.pad.adapter.GoodsAdapter;
import ys.app.pad.adapter.GoodsCollectMoneyAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ActivityGoodsBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.pad_adapter.CollectMoneyNativeGoodsAdapter;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.DeleteDialog;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

/**
 * Created by admin on 2017/6/7.
 */

public class GoodsActivityViewModel extends BaseActivityViewModel {

    private List<GoodTypeInfo> mGoodTypeInfos = new ArrayList<>();
    private RxManager mRxManager;
    private int mLastPosition;
    private GoodsActivity mActivity;
    private ActivityGoodsBinding mBinding;
    private LoadMoreFooterView loadMoreFooterView;
    private GoodsAdapter mAdapter;
    private GoodsCollectMoneyAdapter inventoryAdapter;
    private long mId;
    private ApiClient<BaseListResult<GoodInfo>> mListClient;
    private final ApiClient<BaseResult> mApiClient;
    private CustomDialog mDeleteDialog;
    private DeleteDialog mConfirmDeleteDialog;
    private int mFromIntent;
    private ApiClient<BaseListResult<GoodInfo>> mGoodsClient;
    private ApiClient<BaseListResult> goodsTypeClient;


    public GoodsActivityViewModel(GoodsActivity activity, ActivityGoodsBinding binding, int searchFromIntent) {
        this.mActivity = activity;
        this.mBinding = binding;
        mRxManager = new RxManager();
        this.mListClient = new ApiClient<>();
        this.mApiClient = new ApiClient<BaseResult>();
        this.mGoodsClient = new ApiClient<BaseListResult<GoodInfo>>();
        this.mFromIntent =searchFromIntent;
    }


    public void init() {

        GoodTypeInfoDao goodTypeInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getGoodTypeInfoDao();
        mGoodTypeInfos.addAll(goodTypeInfoDao.loadAll());
        if (mGoodTypeInfos == null || mGoodTypeInfos.size() == 0) {
            loadGoodsTypeHttp();
        }else{
            setGoodsDataTitle();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == Constants.type_updateGoods_success || integer == Constants.type_addGood_success) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                    startHttp = 0;
                    loadHttpData();
                }
            }
        });

    }

    /**
     * 获取商品标题数据
     */
    private void loadGoodsTypeHttp() {
        if (goodsTypeClient == null) goodsTypeClient = new ApiClient<>();
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");

        goodsTypeClient.reqApi("goodsType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                        if (result.isSuccess()) {

                            mGoodTypeInfos = result.getData();
                            setGoodsDataTitle();
                        } else {
                            showToast(mActivity, "获取数据失败");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        showToast(mActivity, "获取数据失败");
                    }
                });
    }


    /**
     * 加载商品导航标题
     */
    private void setGoodsDataTitle(){
        if (mGoodTypeInfos!=null){
            final CollectMoneyNativeGoodsAdapter adapter = new CollectMoneyNativeGoodsAdapter( mActivity,mGoodTypeInfos);
            mBinding.rvCollectMoneyTrade.setLayoutManager(new LinearLayoutManager(mActivity));
            mGoodTypeInfos.get(0).setSelect(1);
            mBinding.rvCollectMoneyTrade.setAdapter(adapter);
            adapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (mLastPosition == position) return;
                    mGoodTypeInfos.get(position).setSelect(1);
                    startHttp=0;
                    firstCome =true;
                    mId=mGoodTypeInfos.get(position).getId();
                    loadHttpData();
                    if (mLastPosition != -1) {
                        mGoodTypeInfos.get(mLastPosition).setSelect(0);
                    }
                    mLastPosition = position;
                    adapter.notifyDataSetChanged();

                }
            });
            firstCome =true;
            mId=mGoodTypeInfos.get(0).getId();
            initGoodsList();
        }
    }

    public void initGoodsList() {
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



        if (Constants.intent_form_shangpin_kucunliebiao == mFromIntent) {
            inventoryAdapter = new GoodsCollectMoneyAdapter(mActivity);
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
            mBinding.recyclerView.setIAdapter(inventoryAdapter);
            inventoryAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int position) {
                    showDeleteDialog(position);
                }
            });
            inventoryAdapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    turnDetail(inventoryAdapter.getList(),position);
                }
            });
            mBinding.recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (loadMoreFooterView.canLoadMore() && inventoryAdapter.getItemCount() > 0) {
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                        loadHttpData();
                    }
                }
            });
            loadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
                @Override
                public void onRetry(LoadMoreFooterView view) {
                    if (loadMoreFooterView.canLoadMore() && inventoryAdapter.getItemCount() > 0) {
                        loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                        loadHttpData();
                    }
                }
            });
        }else {
            mAdapter = new GoodsAdapter(mActivity,mFromIntent);
            mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 4));
            mBinding.recyclerView.setIAdapter(mAdapter);

            mAdapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    turnDetail(mAdapter.getList(),position);
                }
            });
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
        }



        loadHttpData();

    }

    private void turnDetail(List<GoodInfo> list, int position){
        Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
        Bundle bundle = new Bundle();
        if (list != null && !list.isEmpty() && list.size() > position) {
            bundle.putSerializable(Constants.intent_info, list.get(position));
        }
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
    }

    @Override
    public void reloadData(View view) {
        loadHttpData();
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataList();
        } else {//无网显示断网连接
            showToast(mActivity, Constants.network_error_warn);
            afterRefreshAndLoadMoreFailed(mBinding.recyclerView, loadMoreFooterView);
        }
    }

    public void getDataList() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("typeId", mId + "");
        params.put("start", startHttp + "");
        if (Constants.intent_form_shangpin_cuxiaoliebiao == mFromIntent){
            params.put("isPromotion", "1");
        }
        params.put("limit","28");

        mListClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        if (result.isSuccess()) {
                            List<GoodInfo> data = result.getData();
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerView, loadMoreFooterView);
                            } else {
                                if (Constants.intent_form_shangpin_kucunliebiao == mFromIntent) {
                                    inventoryAdapter.setList(startHttp, data);
                                }else{
                                    mAdapter.setList(startHttp, data);
                                }
                                afterRefreshAndLoadMoreItemNum(data, mBinding.recyclerView, loadMoreFooterView,28);
                            }

                        } else {
                            showToast(AppApplication.getAppContext(), result.getErrorMessage());
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

    private void showDeleteDialog(final int position) {
        if (mActivity == null) {
            return;
        }
        if (mDeleteDialog == null) {
            mDeleteDialog = new CustomDialog(mActivity);
            mDeleteDialog.setContent("确定删除该商品?");
            mDeleteDialog.setCancelVisiable();
        }

        mDeleteDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteDialog.dismiss();
                if (mConfirmDeleteDialog == null){
                    mConfirmDeleteDialog = new DeleteDialog(mActivity);
                }
                mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
                    @Override
                    public void verificationPwd(boolean b) {
                        if (b){
                            if (mConfirmDeleteDialog != null){
                                mConfirmDeleteDialog.dismiss();
                            }
                            verSuc();
                        }else {
                            Toast.makeText(mActivity,"密码输入错误请重新输入",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDismiss() {
                        mConfirmDeleteDialog = null;
                    }
                });
                mConfirmDeleteDialog.show();
            }

            private void verSuc() {
                mActivity.showRDialog();
                Map<String, String> params = new HashMap<>();
                if (Constants.intent_form_shangpin_kucunliebiao == mFromIntent){
                    params.put("id", inventoryAdapter.getList().get(position).getId() + "");
                }else{
                    params.put("id", mAdapter.getList().get(position).getId() + "");
                }
                params.put("shopId", SpUtil.getShopId() );
                params.put("branchId", SpUtil.getBranchId()+"");
                params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
                params.put("status", "4");
                mApiClient.reqApi("updateGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                        .updateUI(new Callback<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult baseResult) {
                                if (baseResult.isSuccess()) {
                                    mActivity.hideRDialog();
                                     if (Constants.intent_form_shangpin_kucunliebiao == mFromIntent){
                                         inventoryAdapter.removeData(position);
                                     }else{
                                         mAdapter.removeData(position);
                                     }

                                } else {
                                    showToast(mActivity, baseResult.getErrorMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                if (mConfirmDeleteDialog != null){
                                    mConfirmDeleteDialog.dismiss();
                                }
                                mDeleteDialog.dismiss();
                                mActivity.hideRDialog();
                                showToast(mActivity, e.getMessage());
                            }
                        });
            }
        });

        mDeleteDialog.show();
    }

    public void setScanResult(String barCode) {
        queryGoodsHttp(barCode);
    }

    /**
     * 根据扫描结果查询商品
     *
     * @param barCode
     */
    private void queryGoodsHttp(String barCode) {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("barCode", barCode);
        mGoodsClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        mActivity.hideRDialog();
                        List<GoodInfo> data = result.getData();
                        if (result.isSuccess()) {
                            if (data.size() > 0) {
                                GoodInfo goodInfo = data.get(0);
                                operateScan(goodInfo);
                            } else {
                                //showTurnToAddActivityDialog();
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                        mActivity.showToast("网络异常");
                    }
                });

    }
    private void operateScan(GoodInfo goodInfo){
        if (mFromIntent == Constants.intent_form_shangpin_ruku){
            Intent intent = new Intent(mActivity, AddInventoryActivity.class);
            intent.putExtra(Constants.intent_info, goodInfo);
            mActivity.startActivity(intent);
        }else if (Constants.intent_form_shangpin_chuku== mFromIntent){
            Intent intent = new Intent(mActivity, TakeOutInventoryActivity.class);
            intent.putExtra(Constants.intent_info, goodInfo);
            mActivity.startActivity(intent);
        }else if(Constants.intent_form_shangpin_kucunliebiao== mFromIntent){
            Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.intent_info, goodInfo);
            bundle.putString(Constants.intent_flag, "1");
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
        }else{
            Intent intent = new Intent(mActivity, AddInventoryActivity.class);
            intent.putExtra(Constants.intent_info, goodInfo);
            mActivity.startActivity(intent);
        }

    }


    public void clear() {
        if (null != mGoodTypeInfos && mLastPosition != -1 && mGoodTypeInfos.size() > mLastPosition) {
            mGoodTypeInfos.get(mLastPosition).setSelect(0);
        }
    }
    public void reset() {
        if (mListClient != null) {
            mListClient.reset();
            mListClient = null;
        }
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
        mActivity = null;
        mAdapter = null;
        mDeleteDialog = null;
    }

}
