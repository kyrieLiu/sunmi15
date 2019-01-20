package ys.app.pad.viewmodel;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.greendao.ServiceTypeInfoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.AppApplication;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.ServiceActivity;
import ys.app.pad.activity.ServiceDetailActivity;
import ys.app.pad.adapter.ServiceAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ActivityServiceBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.pad_adapter.CollectMoneyNativeServiceAdapter;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

/**
 * Created by admin on 2017/6/7.
 */

public class ServiceActivityViewModel extends BaseActivityViewModel {



    private final ActivityServiceBinding mBinding;
    private ServiceActivity mActivity;
    private RxManager mRxManager;
    private ApiClient<BaseListResult> mClient;
    private final int mSearchFormIntent;
    private ApiClient<BaseListResult> listClient;
    private List<ServiceTypeInfo> serviceTypeInfos=new ArrayList<>();
    private int serviceLastPostion;
    private long serviceId;
    private ServiceAdapter serviceListAdapter;
    private ApiClient<BaseListResult<ServiceInfo>> serviceClient;
    private LoadMoreFooterView loadMoreFooterView;
    private CollectMoneyNativeServiceAdapter titleAdapter;
    private CustomDialog mDeleteDialog;
    private ApiClient<BaseResult> mDeleteApiClient;

    public ServiceActivityViewModel(ServiceActivity activity, ActivityServiceBinding binding,int searchFormIntent) {
        this.mBinding = binding;
        this.mActivity = activity;
        this.mSearchFormIntent = searchFormIntent;
        this.mDeleteApiClient = new ApiClient<>();
        if (serviceClient==null){
            serviceClient=new ApiClient<>();
        }
        this.mClient = new ApiClient<>();
        init();
    }


    public void init() {

        if (mRxManager==null){
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.type_update_service, new Action1<Integer>() {

            @Override
            public void call(Integer integer) {
                    startHttp=0;
                    loadServiceHttpData();
            }
        });
        ServiceTypeInfoDao serviceTypeInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
        serviceTypeInfos.addAll(serviceTypeInfoDao.loadAll());
        if (serviceTypeInfos == null || serviceTypeInfos.isEmpty()) {//没有初始化成功,进行初始化
            loadServiceTypeHttp();
            return;
        }
            setServiceTitleData();
    }

    /**
     * 加载服务导航标题
     */
    private void setServiceTitleData(){
        if (serviceTypeInfos!=null){
            serviceTypeInfos.get(0).setSelect(1);
            titleAdapter= new CollectMoneyNativeServiceAdapter( serviceTypeInfos,mActivity);

            mBinding.rvCollectMoneyService.setLayoutManager(new LinearLayoutManager(mActivity));
            mBinding.rvCollectMoneyService.setAdapter(titleAdapter);
            titleAdapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (serviceLastPostion == position) return;
                    serviceTypeInfos.get(position).setSelect(1);

                    serviceId=serviceTypeInfos.get(position).getId();
                    firstCome =true;
                    startHttp=0;
                    loadServiceHttpData();
                    serviceTypeInfos.get(serviceLastPostion).setSelect(0);
                    serviceLastPostion = position;
                    titleAdapter.notifyDataSetChanged();

                }
            });
            initService();//初始化服务列表
        }
    }
    /**
     * 加载服务列表
     */
    public void initService() {

        mBinding.recyclerViewService.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                startHttp=0;
                loadServiceHttpData();
            }
        });

        loadMoreFooterView = (LoadMoreFooterView) mBinding.recyclerViewService.getLoadMoreFooterView();

        serviceListAdapter = new ServiceAdapter(mActivity);
        if (Constants.intent_form_fuwu_cuxiaoshezhi==mSearchFormIntent){
            serviceListAdapter.setPromot(true);
        }
        mBinding.recyclerViewService.setLayoutManager(new GridLayoutManager(mActivity, 4));
        mBinding.recyclerViewService.setIAdapter(serviceListAdapter);
        loadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            @Override
            public void onRetry(LoadMoreFooterView view) {
                if (loadMoreFooterView.canLoadMore() && serviceListAdapter.getItemCount() > 0) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadServiceHttpData();
                }
            }
        });
        mBinding.recyclerViewService.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMoreFooterView.canLoadMore() && serviceListAdapter.getItemCount() > 0) {
                    loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadServiceHttpData();
                }
            }
        });

        serviceListAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                List<ServiceInfo> list = serviceListAdapter.getList();
                Intent intent = new Intent(mActivity, ServiceDetailActivity.class);
                intent.putExtra(Constants.intent_service_info, list.get(position));
                intent.putExtra(Constants.intent_service_position, position);
                mActivity.startActivity(intent);

            }
        });
        if (Constants.intent_form_fuwu_kucunliebiao == mSearchFormIntent) {
            serviceListAdapter.setLongClicklistener(new OnItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, int position) {
                    if (!mActivity.isFinishing()) {
                        showDeleteDialog(position);
                    }
                }
            });
        }


        serviceId=serviceTypeInfos.get(0).getId();
        firstCome = true;
        startHttp = 0;
        loadServiceHttpData();

    }

    /**
     * 获取服务标题数据
     */
    private void loadServiceTypeHttp() {
        if (listClient == null) {
            listClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        listClient.reqApi("serviceType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                        if (result.isSuccess()) {
                            long nowTime = System.currentTimeMillis();
                            ServiceTypeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
                            serviceTypeInfos = result.getData();
                            if (serviceTypeInfos != null && !serviceTypeInfos.isEmpty()) {
                                for (ServiceTypeInfo info : serviceTypeInfos) {
                                    info.setRequestTime(nowTime);
                                }
                            }
                            dao.deleteAll();
                            dao.insertInTx(serviceTypeInfos);
                        }
                    }
                })
                .updateUI(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                        if (result.isSuccess()) {
                            setServiceTitleData();
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
    private void loadServiceHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getServiceDataList();
        } else {//无网显示断网连接
            showToast(mActivity, Constants.network_error_warn);
            afterRefreshFailed(mBinding.recyclerViewService);
        }
    }
    public void getServiceDataList() {
        Map<String, String> params = new HashMap<>();
        params.put("start", startHttp+"");
        params.put("typeId", serviceId + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        if (Constants.intent_form_fuwu_cuxiaoliebiao == mSearchFormIntent){
            params.put("isPromotion", "1");
        }
        params.put("limit",limit_20+"");


        serviceClient.reqApi("queryServiceList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceInfo> result) {
                        if (result.isSuccess()) {
                            List<ServiceInfo> data = result.getData();
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreFooterView);
                            } else {
                                serviceListAdapter.setList(startHttp,data);
                                afterRefreshAndLoadMoreItemNum(data, mBinding.recyclerViewService, loadMoreFooterView,limit_20);
                            }

                        } else {
                            showToast(AppApplication.getAppContext(), result.getErrorMessage());
                            afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreFooterView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        afterRefreshFailed(mBinding.recyclerViewService);
                    }
                });
    }

    private void showDeleteDialog(final int position) {
        if (mActivity == null) {
            return;
        }
        if (mDeleteDialog == null) {
            mDeleteDialog = new CustomDialog(mActivity);
            mDeleteDialog.setContent("确定删除该服务?");
            mDeleteDialog.setCancelVisiable();
        }

        mDeleteDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.showRDialog();
                Map<String, String> params = new HashMap<>();
                params.put("id", serviceListAdapter.getList().get(position).getId() + "");
                params.put("shopId", SpUtil.getShopId() );
                params.put("branchId",SpUtil.getBranchId()+"");
                params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
                params.put("status", "4");
                mDeleteApiClient.reqApi("updateService", params, ApiRequest.RespDataType.RESPONSE_JSON)
                        .updateUI(new Callback<BaseResult>() {
                            @Override
                            public void onSuccess(BaseResult baseResult) {
                                mActivity.hideRDialog();
                                mDeleteDialog.dismiss();
                                if (baseResult.isSuccess()) {
                                    serviceListAdapter.removeData(position);
                                } else {
                                    showToast(mActivity, baseResult.getErrorMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                mDeleteDialog.dismiss();
                                mActivity.hideRDialog();
                                showToast(mActivity, e.getMessage());
                            }
                        });

            }
        });

        mDeleteDialog.show();
    }

    public void clear() {

        if (null != serviceTypeInfos && serviceLastPostion != -1 && serviceTypeInfos.size() > serviceLastPostion) {
            serviceTypeInfos.get(serviceLastPostion).setSelect(0);
        }
        mActivity = null;
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
        if (mClient != null) {
            mClient.reset();
            mClient = null;
        }
        titleAdapter=null;
        if (serviceTypeInfos!=null){
            serviceTypeInfos.clear();
            serviceTypeInfos=null;
        }
    }

}
