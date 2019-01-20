package ys.app.pad.viewmodel.manage;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.greendao.NumCardEntityInfoDao;
import com.greendao.ServiceTypeInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.AppApplication;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.NumCardDetailActivity;
import ys.app.pad.adapter.manage.NumCardAdapter;
import ys.app.pad.adapter.manage.NumCardTopAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ActivityNumCardBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.itemmodel.CardNumberList2Bean;
import ys.app.pad.itemmodel.NumCardEntityInfo;
import ys.app.pad.model.BaseDataResult;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.pad_adapter.CollectMoneyNativeServiceAdapter;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.autoview.HeaderRecyclerAdapter;
import ys.app.pad.widget.autoview.NumCardParamBean;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

/**
 * Created by liuyin on 2017/12/1.
 */

public class NumCardDetailViewModel extends BaseActivityViewModel {
    private final ActivityNumCardBinding mBinding;
    private NumCardDetailActivity mActivity;
    private RxManager mRxManager;
    private ApiClient<BaseDataResult> mClient;
    private ApiClient<BaseListResult> listClient;
    private List<ServiceTypeInfo> serviceTypeInfos=new ArrayList<>();
    private int serviceLastPostion;
    private long serviceId;
    private NumCardAdapter serviceListAdapter;
    private ApiClient<BaseListResult<ServiceInfo>> serviceClient;
    private LoadMoreFooterView loadMoreFooterView;
    private CollectMoneyNativeServiceAdapter titleAdapter;
    private Map<String, String> params = new HashMap<>();
    private NumCardTopAdapter cardTopAdapter;
    private  NumCardEntityInfoDao numCardEntityInfoDao;

    private RecyclerView showRecyclerView;
    List<NumCardParamBean> numCardBeanList;
    HeaderRecyclerAdapter showAdapter;
    private QueryBuilder qb;

    public NumCardDetailViewModel(NumCardDetailActivity activity, ActivityNumCardBinding binding) {
        this.mBinding = binding;
        this.mActivity = activity;
        if (serviceClient==null){
            serviceClient=new ApiClient<>();
        }
        this.mClient = new ApiClient<>();
        numCardEntityInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardEntityInfoDao();
        init();
    }
    public void setNumCardInformation(NumCardListInfo info){
        mBinding.etCardName.setText(info.getName());
        mBinding.etCardMoney.setText(info.getRealAmt()+"");
        List<CardNumberList2Bean> list=info.getCardNumberList2();
        for (CardNumberList2Bean itemModle:list){
            NumCardEntityInfo unique = new NumCardEntityInfo();
            unique.setNum(itemModle.getNumber());
            unique.setId(itemModle.getProductId());
            unique.setName(itemModle.getProductName());
            unique.setRealAmt(itemModle.getRealAmt());
            numCardEntityInfoDao.insert(unique);
        }
//        updateRecyclerView();
    }


    public void init() {
        showRecyclerView = mBinding.showRecyclerView;
        numCardBeanList = new ArrayList<>();
        showAdapter = new HeaderRecyclerAdapter(null,mActivity);
        qb = numCardEntityInfoDao.queryBuilder();

        if (mRxManager==null){
            mRxManager = new RxManager();
        }
//        mRxManager.on(Constants.type_card, new Action1<Integer>() {
//
//            @Override
//            public void call(Integer integer) {
//                if (integer==0){
//                    updateRecyclerView();
//                }
//            }
//        });
        mRxManager.on(Constants.infomations, new Action1<NumCardEntityInfo>() {
            @Override
            public void call(NumCardEntityInfo info) {
                showAdapter.setList(info);
            }
        });

//        cardTopAdapter = new NumCardTopAdapter(mActivity);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mBinding.rvNumCardTop.setLayoutManager(linearLayoutManager);
//        mBinding.rvNumCardTop.setAdapter(cardTopAdapter);


        //===============
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int spanSize;
                if (showAdapter.getItemViewType(position) == showAdapter.TYPE_HEAD){
                    spanSize = 2;
                }else {
                    spanSize = 1;
                }
                return spanSize;
            }
        });
        showRecyclerView.setLayoutManager(gridLayoutManager);
        showRecyclerView.setAdapter(showAdapter);
        showAdapter.setListener(new HeaderRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int parentPosition, int childPosition, int realPosition) {
                //先通过位置确定对象
                List<NumCardParamBean> list = showAdapter.getList();
                NumCardEntityInfo cardEntityInfo = list.get(parentPosition).getLis().get(childPosition);
                //然后对数据库进行删除同步
                qb.where(NumCardEntityInfoDao.Properties.Id.eq(cardEntityInfo.getId()));
                if (qb.unique()!=null) {
                    NumCardEntityInfo entity = (NumCardEntityInfo) qb.unique();
                    numCardEntityInfoDao.delete(entity);
                }

                showAdapter.removeData(parentPosition,childPosition,realPosition);
            }
        });
        //===============

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
            //updateRecyclerView();
        }
    }
    private void updateRecyclerView(){
        List<NumCardEntityInfo> numCardList= numCardEntityInfoDao.loadAll();
        cardTopAdapter.setList(numCardList);
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

        serviceListAdapter = new NumCardAdapter(mActivity);
        mBinding.recyclerViewService.setLayoutManager(new GridLayoutManager(mActivity, 2));
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
        params.clear();
        params.put("branchId", SpUtil.getBranchId()+"");
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
        params.clear();
        params.put("start", "0");
        params.put("limit", "1000");
        params.put("typeId", serviceId + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");

        isLoadingVisible.set(true);
        serviceClient.reqApi("queryServiceList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceInfo> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            List<ServiceInfo> data = result.getData();
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreFooterView);
                            } else {
                                serviceListAdapter = new NumCardAdapter(mActivity);
                                mBinding.recyclerViewService.setLayoutManager(new GridLayoutManager(mActivity, 2));
                                mBinding.recyclerViewService.setIAdapter(serviceListAdapter);
                                serviceListAdapter.setList(data);
                                afterRefreshAndLoadMoreSuccess(data, mBinding.recyclerViewService, loadMoreFooterView);
                            }

                        } else {
                            showToast(AppApplication.getAppContext(), result.getErrorMessage());
                            afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreFooterView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isLoadingVisible.set(false);
                        afterRefreshFailed(mBinding.recyclerViewService);
                    }
                });
    }

    /**
     * 服务搜索
     *
     * @param s
     */
    public void getSearchServiceHttp(String s) {

        params.clear();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("name", s);
        params.put("limit","1000");
        isLoadingVisible.set(true);
        serviceClient.reqApi("queryServiceList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceInfo> result) {
                        isLoadingVisible.set(false);
                        List<ServiceInfo> data = result.getData();
                        if (result.isSuccess()) {

                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreFooterView);
                            } else {
                                serviceListAdapter = new NumCardAdapter(mActivity);
                                mBinding.recyclerViewService.setLayoutManager(new GridLayoutManager(mActivity, 2));
                                mBinding.recyclerViewService.setIAdapter(serviceListAdapter);
                                serviceListAdapter.setList(data);
                                afterRefreshAndLoadMoreSuccess(data, mBinding.recyclerViewService, loadMoreFooterView);
                            }
                        } else {
                            showToast(AppApplication.getAppContext(), result.getErrorMessage());
                            afterRefreshAndLoadMoreSuccess(data, mBinding.recyclerViewService, loadMoreFooterView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isLoadingVisible.set(false);
                        afterRefreshFailed(mBinding.recyclerViewService);
                    }
                });

    }

    @Override
    public void reloadData(View view) {
        super.reloadData(view);
        loadServiceHttpData();
    }

    public void commitData(){
        params.clear();
        List<NumCardEntityInfo> numCardList= numCardEntityInfoDao.loadAll();
        if (numCardList.size()<=0){
            showToast(mActivity,"请输入服务次数");
            return;
        }
        String json=new Gson().toJson(numCardList);
        String cardName=mBinding.etCardName.getText().toString();
        if (!StringUtils.isEmpty(cardName)){
            params.put("name",cardName);
        }
        String realAmt=mBinding.etCardMoney.getText().toString();
        if (StringUtils.isEmpty(realAmt)){
            showToast(mActivity,"请输入卡片金额");
            return;
        }
        double money=Double.parseDouble(realAmt);
        if (money<=0){
            showToast(mActivity,"套餐金额需大于0元");
            return;
        }
        params.put("realAmt",realAmt);
        params.put("shopId",SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("classification","1");
        params.put("cardNumberList",json);

        mActivity.showRDialog();
        mClient.reqApi("insertNumberCard", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseDataResult>() {
                    @Override
                    public void onSuccess(BaseDataResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_addVipCard_success);
                            mActivity.finish();
                            showToast(mActivity,"添加成功");
                        } else {
                            showToast(AppApplication.getAppContext(), result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });


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
        numCardEntityInfoDao.deleteAll();
    }
}
