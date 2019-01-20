package ys.app.pad.viewmodel;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.greendao.EmployeeInfoDao;
import com.greendao.GoodTypeInfoDao;
import com.greendao.ServiceTypeInfoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.AppApplication;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.AddGoodsActivity;
import ys.app.pad.activity.CollectMoneyActivity;
import ys.app.pad.activity.CommitOrderActivity;
import ys.app.pad.activity.PayListActivity;
import ys.app.pad.activity.SearchActivity;
import ys.app.pad.adapter.CollectShopCarAdapter;
import ys.app.pad.adapter.GoodsCollectMoneyAdapter;
import ys.app.pad.adapter.SearchGoodsAdapter;
import ys.app.pad.adapter.SearchServiceAdapter;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.adapter.ServiceAdapter;
import ys.app.pad.adapter.ServiceNumCardAdapter;
import ys.app.pad.adapter.ServiceNumCardChildAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ActivityCollectMoneyBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.itemmodel.CardNumberList2Bean;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.CommitOrderTempInfo;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.model.VipInfo;
import ys.app.pad.pad_adapter.CollectMoneyNativeGoodsAdapter;
import ys.app.pad.pad_adapter.CollectMoneyNativeServiceAdapter;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CardSelectDialog;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.SearchDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;

import static ys.app.pad.R.id.recyclerView;

/**
 * Created by aaa on 2017/3/15.
 */

public class CollectMoneyViewModel extends BaseActivityViewModel {
    private CollectMoneyActivity mActivity;
    private ActivityCollectMoneyBinding mBinding;
    private ApiClient<BaseListResult<GoodInfo>> mGoodsClient;
    private ApiClient<BaseResult> mClient;
    private ApiClient<BaseListResult> listClient;
    private ApiClient<BaseListResult> goodsTypeClient;
    private ApiClient<BaseResult> mInsertOrderClient;
    private ApiClient<BaseListResult<GoodInfo>> mGoodsListClient;
    private ApiClient<BaseListResult<ServiceInfo>> mServiceListClient;
    private ApiClient<BaseListResult<GoodInfo>> mListClient;
    private ApiClient<BaseListResult<ServiceInfo>> serviceClient;
    private ApiClient<BaseListResult<NumCardListInfo>> cardClient;
    private ApiClient<BaseListResult<EmployeeInfo>> mEmployeeListClient;
    private ApiClient mVipClient;
    private RxManager rxManager;
    public static List<CommitOrderTempInfo> commitOrderTempInfos = new ArrayList<>();
    private int serviceNum, goodNum, mSearchType, mLastPosition, serviceLastPostion = -1;
    private double totalAmt;
    private CustomDialog mBackDialog;
    private SearchDialog mSearchDialog;
    private RecyclerView mSearchRecyclerView;
    private SearchGoodsAdapter goodsAdapter;
    private SearchServiceAdapter serviceAdapter;
    private CustomDialog mCustomDialog;
    private List<GoodTypeInfo> goodTypeInfos;
    private List<ServiceTypeInfo> serviceTypeInfos;
    private boolean mIsGuadan;//判断商品是否为打开状态,判断服务是否为打开状态,判别是商品列表还是服务列表,是否挂单
    private GoodsCollectMoneyAdapter mAdapter;
    private ServiceAdapter serviceListAdapter;
    private CollectShopCarAdapter carAdapter;
    private LoadMoreFooterView loadMoreFooterView, loadMoreServiceView;
    private long goodId, serviceId;
    private ImageView iv_noData;//暂无数据
    private RelativeLayout rl_progress;//网络加载等待
    private String nonMemberName = "", nonMemberPhone = "", search_key;//非会员名称,非会员电话,搜索商品或服务的关键字
    private Map<String, String> params = new HashMap<>();
    private EmployeeInfo mEmployeeInfo;
    private VipInfo vipInfo;
    public ObservableBoolean showNavigateService = new ObservableBoolean(true);//是否显示服务菜单列表
    public ObservableBoolean showNavigateGoods = new ObservableBoolean(true);//是否显示商品菜单列表
    public ObservableInt showView = new ObservableInt(1);//区别展示商品,服务,次卡列表信息
    public ObservableInt vipType = new ObservableInt(3);//1代表折扣会员,2代表次卡会员,3代表折扣会员
    public ObservableBoolean showCard = new ObservableBoolean(true);
    public ObservableField<String> cardName = new ObservableField<String>();
    private List<NumCardListInfo> numCardListInfoList;
    private NumCardListInfo numCardInfo;
    private ServiceNumCardAdapter numCardAdapter;
    private ServiceNumCardChildAdapter numServiceAdapter;
    private int numCardID;//次卡ID
    public static List<String> orderNums = new ArrayList<>();
    private CardSelectDialog cardSelectDialog;
    private VipCardInfo vipCardInfo;
    private CollectMoneyNativeGoodsAdapter nativeGoodsAdapter;
    private CollectMoneyNativeServiceAdapter nativeServiceAdapter;


    public CollectMoneyViewModel(CollectMoneyActivity activity, ActivityCollectMoneyBinding binding, ImageView blueSearchIv) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
        this.mVipClient = new ApiClient();
        this.mInsertOrderClient = new ApiClient<>();
        this.mGoodsClient = new ApiClient<BaseListResult<GoodInfo>>();
        this.mGoodsListClient = new ApiClient<>();
        this.mServiceListClient = new ApiClient<>();
        cardClient = new ApiClient<>();
        registerBus();
        blueSearchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
        mSearchType = Constants.intent_type_goods;
    }

    /**
     * 初始化数据
     */
    public void initData() {
        initShopCar();//初始化购物车
        GoodTypeInfoDao goodTypeInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getGoodTypeInfoDao();
        goodTypeInfos = goodTypeInfoDao.loadAll();
        if (goodTypeInfos == null || goodTypeInfos.isEmpty()) {//没有初始化成功,进行初始化
            loadGoodsTypeHttp();
        } else {
            setGoodsDataTitle();
        }
        ServiceTypeInfoDao serviceTypeInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao();
        serviceTypeInfos = serviceTypeInfoDao.loadAll();
        if (serviceTypeInfos == null || serviceTypeInfos.isEmpty()) {//没有初始化成功,进行初始化
            loadServiceTypeHttp();
        } else {
            setServiceTitleData();
        }

        listenModuleView();//初始化侧边栏
        initNumCardService();//初始化次卡和次卡服务
    }

    /**
     * 获取商品标题数据
     */
    private void loadGoodsTypeHttp() {
        if (goodsTypeClient == null) goodsTypeClient = new ApiClient<>();
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");

        goodsTypeClient.reqApi("goodsType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                        Log.d("Tag", "类型获取成功");
                        if (result.isSuccess()) {

                            goodTypeInfos = result.getData();


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
     * 获取服务标题数据
     */
    private void loadServiceTypeHttp() {
        if (listClient == null) listClient = new ApiClient<>();
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");

        listClient.reqApi("serviceType", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult>() {
                    @Override
                    public void onSuccess(BaseListResult result) {
                        if (result.isSuccess()) {
                            serviceTypeInfos = result.getData();
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


    /**
     * 加载商品导航标题
     */
    private void setGoodsDataTitle() {
        Log.d("Tag", "goodTypeInfos==" + goodTypeInfos);
        nativeGoodsAdapter = new CollectMoneyNativeGoodsAdapter(mActivity, goodTypeInfos);
        mBinding.rvCollectMoneyTrade.setLayoutManager(new LinearLayoutManager(mActivity));
        goodTypeInfos.get(0).setSelect(1);
        mBinding.rvCollectMoneyTrade.setAdapter(nativeGoodsAdapter);
        nativeGoodsAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mLastPosition != position) {
                    goodTypeInfos.get(position).setSelect(1);
                    if (mLastPosition != -1) goodTypeInfos.get(mLastPosition).setSelect(0);
                    mLastPosition = position;
                    nativeGoodsAdapter.notifyDataSetChanged();
                    if (showView.get() != 1) {//如果上次是服务列表,切换到商品后将服务列表更新
                        showView.set(1);
                        serviceTypeInfos.get(serviceLastPostion).setSelect(0);
                        nativeServiceAdapter.notifyDataSetChanged();
                        serviceLastPostion = -1;
                    }
                    goodId = goodTypeInfos.get(position).getId();
                    startHttp = 0;
                    firstCome = true;
                    loadHttpData();
                }

            }
        });
        initGoods();//开始加载商品列表
    }

    /**
     * 加载服务导航标题
     */
    private void setServiceTitleData() {
        if (serviceTypeInfos != null) {
            final ServiceTypeInfo serviceTypeInfo = new ServiceTypeInfo();
            serviceTypeInfo.setName("次卡");
            serviceTypeInfo.setId(-1);
            serviceTypeInfos.add(0, serviceTypeInfo);
            nativeServiceAdapter = new CollectMoneyNativeServiceAdapter(serviceTypeInfos, mActivity);
            mBinding.rvCollectMoneyService.setLayoutManager(new LinearLayoutManager(mActivity));
            mBinding.rvCollectMoneyService.setAdapter(nativeServiceAdapter);
            nativeServiceAdapter.setListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (serviceLastPostion != position) {
                        serviceTypeInfos.get(position).setSelect(1);
                        if (serviceLastPostion != -1)
                            serviceTypeInfos.get(serviceLastPostion).setSelect(0);
                        serviceLastPostion = position;
                        nativeServiceAdapter.notifyDataSetChanged();
                        if (showView.get() == 1) {//如果上次是商品列表,切换到服务后将商品列表更新
                            goodTypeInfos.get(mLastPosition).setSelect(0);
                            nativeGoodsAdapter.notifyDataSetChanged();
                            mLastPosition = -1;
                        }
                        serviceId = serviceTypeInfos.get(position).getId();
                        firstCome = true;
                        startHttp = 0;
                        if (position == 0) {//次卡
                            if (showView.get() != 3) {
                                showView.set(3);
                            }
                            loadCardHttpData();//加载次卡列表
                        } else {//普通服务
                            if (showView.get() != 2) {
                                showView.set(2);
                            }
                            loadServiceHttpData();//加载服务
                        }
                    }


                }
            });
            initService();//初始化服务列表
        }
    }

    /**
     * 监听侧边栏按钮
     */
    private void listenModuleView() {
        mBinding.rlCollectMoneyGoodsTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showNavigateGoods.get()) {//如果非打开状态,则可展开条目
                    showNavigateGoods.set(false);
                } else {
                    showNavigateGoods.set(true);
                }
            }
        });
        mBinding.rlCollectMoneyServiceTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showNavigateService.get()) {//如果非打开状态,则可展开条目
                    showNavigateService.set(false);
                } else {
                    showNavigateService.set(true);
                }
            }
        });

    }

    /**
     * 加载商品列表数据
     */
    private void initGoods() {
        mBinding.recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData();
            }
        });

        loadMoreFooterView = (LoadMoreFooterView) mBinding.recyclerView.getLoadMoreFooterView();

        mAdapter = new GoodsCollectMoneyAdapter(mActivity);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
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

        if (goodTypeInfos != null) {
            mListClient = new ApiClient<>();
            goodId = goodTypeInfos.get(0).getId();
        }
        loadHttpData();

        mAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<GoodInfo> list = mAdapter.getList();

                if (list != null && !list.isEmpty() && list.size() > position) {
                    GoodInfo goodInfo = list.get(position);
                    if (goodInfo.getStockNum() <= 0) {
                        showToast(mActivity, "库存为0,不能加入购物车");
                        return;
                    }
                    insertShopToCar(goodInfo);
                }

            }
        });
    }

    /**
     * 初始化购物车
     */
    private void initShopCar() {
        carAdapter = new CollectShopCarAdapter(mActivity, commitOrderTempInfos);
        mBinding.rvCollectMoneyShopCar.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.rvCollectMoneyShopCar.setAdapter(carAdapter);
        carAdapter.setListener(new CollectShopCarAdapter.OnShopCarClickListener() {
            @Override
            public void shopCarClick() {
                updateBottomMoney();
            }
        });
    }

    /**
     * 初始化次卡和次卡服务
     */
    private void initNumCardService() {
        numServiceAdapter = new ServiceNumCardChildAdapter(mActivity);
        mBinding.rvNumServiceList.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mBinding.rvNumServiceList.setAdapter(numServiceAdapter);
        numServiceAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CardNumberList2Bean cardService = numServiceAdapter.getList().get(position);
                insertShopToCar(cardService);

            }
        });
        numCardAdapter = new ServiceNumCardAdapter(mActivity);
        mBinding.rvNumCardList.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.rvNumCardList.setAdapter(numCardAdapter);
        numCardAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showCard.set(false);
                numCardInfo = numCardListInfoList.get(position);
                List<CardNumberList2Bean> list = numCardInfo.getCardNumberList2();
                numServiceAdapter.setList(list);
                cardName.set("卡片名称: " + numCardInfo.getName());
            }
        });
    }

    /**
     * 更新底部的统计信息
     */
    @SuppressLint("SetTextI18n")
    private void updateBottomMoney() {

        serviceNum = 0;
        goodNum = 0;
        totalAmt = 0.00;
        for (CommitOrderTempInfo info : commitOrderTempInfos) {

            if (!(1 == info.getIsGift())) {//不是赠品
                goodNum += info.getNum();
                if (Constants.is_promotion == info.getIsPromotion()) {
                    totalAmt += info.getNum() * info.getPromotionAmt();
                } else {
                    totalAmt += info.getNum() * info.getPrice();
                }
            }
        }
        mBinding.moneyTv.setText(AppUtil.formatStandardMoney(totalAmt) + " 元");
        if (goodNum < 1) {
            mBinding.bvUnm.setVisibility(View.GONE);
        } else {
            mBinding.bvUnm.setText(goodNum + "");
            mBinding.bvUnm.setVisibility(View.VISIBLE);
        }
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
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("typeId", goodId + "");
        params.put("start", startHttp + "");

        mListClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        if (result.isSuccess()) {
                            List<GoodInfo> data = result.getData();
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerView, loadMoreFooterView);
                            } else {
                                mAdapter.setList(startHttp, data);
                                afterRefreshAndLoadMoreSuccess(data, mBinding.recyclerView, loadMoreFooterView);
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

    @Override
    public void reloadData(View view) {
        super.reloadData(view);
        firstCome = true;
        startHttp = 0;
        if (showView.get() == 1) {
            loadHttpData();
        }
    }

    //将商品加入到购物车
    private void insertShopToCar(Object o) {
            EmployeeInfoDao employeeInfoDao = GreenDaoUtils.getmDaoSession().getEmployeeInfoDao();
            List<EmployeeInfo> list = employeeInfoDao.loadAll();
            if (list != null && list.size() > 0) {
                mEmployeeInfo = list.get(0);
                addToCar(o);
            } else {
                loadEmployeeListHttp(o);

            }
    }

    private void addToCar(Object o) {
        int isPromotion = 0, cardID = 0, stockNum = -1;
        long id = 0, type = 0;
        double promotionAmt = 0, price = 0;
        String name = null;

        if (o instanceof GoodInfo) {//商品
            if (judgeHaveNumCard()) {
                showToast(mActivity, "购物车含有次卡服务,不可加入商品");
                return;
            }
            GoodInfo info = (GoodInfo) o;
            isPromotion = info.getIsPromotion();
            id = info.getId();
            price = info.getRealAmt();
            promotionAmt = info.getPromotionAmt();
            name = info.getName();
            type = 1;
            stockNum = info.getStockNum();
        } else if (o instanceof ServiceInfo) {//服务
            if (judgeHaveNumCard()) {
                showToast(mActivity, "购物车含有次卡服务,不可加入服务");
                return;
            }
            ServiceInfo info = (ServiceInfo) o;
            isPromotion = info.getIsPromotion();
            id = info.getId();
            price = info.getRealAmt();
            promotionAmt = info.getPromotionAmt();
            name = info.getName();
            type = 2;
        } else if (o instanceof CardNumberList2Bean) {//次卡
            CardNumberList2Bean info = (CardNumberList2Bean) o;
            boolean canBuy = judgeCanBuyNumService(info.getCardId());
            if (!canBuy) {
                showToast(mActivity, "购物车含有其他类型的商品,不能购买该次卡服务");
                return;
            }
            isPromotion = 0;
            id = info.getProductId();
            price = info.getRealAmt();
            promotionAmt = info.getRealAmt();
            name = info.getProductName();
            type = 3;
            cardID = info.getCardId();
        }
        CommitOrderTempInfo orderInfo = new CommitOrderTempInfo();
        orderInfo.setId(id);
        orderInfo.setType(type);
        orderInfo.setUserId(mEmployeeInfo.getId());
        if (type != 3) {
            orderInfo.setIsGift(0);
            if (Constants.is_promotion == isPromotion) {
                orderInfo.setPromotionAmt(promotionAmt);
            } else {
                orderInfo.setPrice(price);
            }
        } else {
            orderInfo.setCardID(cardID);
        }

        CommitOrderTempInfo entity = null;
        if (commitOrderTempInfos.contains(orderInfo)) {
            for (int i = 0; i < commitOrderTempInfos.size(); i++) {
                entity = commitOrderTempInfos.get(i);
                boolean accordance = isAccordance(entity, orderInfo);
                if (accordance) break;
            }
            entity.setNum(entity.getNum() + 1);
            entity.setUserId(mEmployeeInfo.getId());
            entity.setUserName(mEmployeeInfo.getName());
            carAdapter.setList(commitOrderTempInfos);

        } else {
            entity = new CommitOrderTempInfo();
            entity.setId(id);
            entity.setUserId(mEmployeeInfo.getId());
            entity.setUserName(mEmployeeInfo.getName());
            entity.setType(type);
            entity.setNum(1);
            entity.setName(name);
            entity.setOriginNum(stockNum);
            entity.setIsPromotion(isPromotion);
            if (Constants.is_promotion == isPromotion) {
                entity.setPromotionAmt(promotionAmt);
            }
            entity.setPrice(price);
            entity.setIsGift(0);
            entity.setCardID(cardID);
            carAdapter.addShop(entity);
            showToast(mActivity, "加入购物车成功");
        }
        updateBottomMoney();
        canShowGuide();
    }

    private void loadEmployeeListHttp(final Object o) {
        if (mEmployeeListClient == null) {
            mEmployeeListClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");

        mEmployeeListClient.reqApi("queryEmployeeList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> result) {
                        if (result.isSuccess()) {
                            List<EmployeeInfo> list = result.getData();

                            long nowTime = System.currentTimeMillis();
                            EmployeeInfoDao dao = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao();

                            if (list != null && !list.isEmpty()) {
                                for (EmployeeInfo info : list) {
                                    info.setRequestTime(nowTime);
                                }

                            }
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                })
                .updateUI(new Callback<BaseListResult<EmployeeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<EmployeeInfo> result) {
                        List<EmployeeInfo> list = result.getData();
                        if (list == null || list.size() == 0) {
                            Toast.makeText(mActivity, "请在员工管理里添加员工", Toast.LENGTH_LONG).show();
                        } else {
                            mEmployeeInfo = list.get(0);
                            addToCar(o);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    //第一次进入显示操作引导
    private void canShowGuide() {

        if (SpUtil.getIsFirtIntoAPP()) {
            mBinding.llCollectMoneyGuide.setVisibility(View.VISIBLE);
            mBinding.llCollectMoneyGuide.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    SpUtil.setIsFirtIntoAPP(false);
                    mBinding.llCollectMoneyGuide.setVisibility(View.GONE);
                }
            });
        }
    }

    /**
     * 加载服务列表
     */
    private void initService() {
        loadMoreServiceView = (LoadMoreFooterView) mBinding.recyclerViewService.getLoadMoreFooterView();
        mBinding.recyclerViewService.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                startHttp = 0;
                loadServiceHttpData();
            }
        });


        serviceListAdapter = new ServiceAdapter(mActivity);
        mBinding.recyclerViewService.setLayoutManager(new GridLayoutManager(mActivity, 2));
        mBinding.recyclerViewService.setIAdapter(serviceListAdapter);
        serviceListAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                insertShopToCar(serviceListAdapter.getList().get(position));

            }
        });

        mBinding.recyclerViewService.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (loadMoreServiceView.canLoadMore() && serviceListAdapter.getItemCount() > 0) {
                    loadMoreServiceView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadServiceHttpData();
                }
            }
        });

    }

    private void loadServiceHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) isLoadingVisible.set(true);
            getServiceDataList();
        } else {//无网显示断网连接
            showToast(mActivity, Constants.network_error_warn);
            afterRefreshFailed(mBinding.recyclerViewService);
        }
    }

    public void getServiceDataList() {
        params.clear();
        params.put("start", startHttp + "");
        params.put("typeId", serviceId + "");
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        if (serviceClient == null) serviceClient = new ApiClient<>();

        params.put("limit", limit_15 + "");

        serviceClient.reqApi("queryServiceList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceInfo> result) {
                        if (result.isSuccess()) {
                            List<ServiceInfo> data = result.getData();
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreServiceView);
                            } else {
                                serviceListAdapter.setList(startHttp, data);
                                afterRefreshAndLoadMoreItemNum(data, mBinding.recyclerViewService, loadMoreServiceView, limit_15);
                            }

                        } else {
                            showToast(AppApplication.getAppContext(), result.getErrorMessage());
                            afterRefreshAndLoadMoreFailed(mBinding.recyclerViewService, loadMoreServiceView);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        afterRefreshFailed(mBinding.recyclerViewService);
                    }
                });
    }

    private void loadCardHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            loadCardList();
        } else {//无网显示断网连接
            showToast(mActivity, Constants.network_error_warn);
            afterRefreshFailed(mBinding.rvNumServiceList);
        }
    }

    public void loadCardList() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("limit", "1000");

        cardClient.reqApi("selectVipCardNumber", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<NumCardListInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<NumCardListInfo> result) {
                        if (result.isSuccess()) {
                            numCardListInfoList = result.getData();
                            numCardAdapter.setList(numCardListInfoList);
                            afterRefreshSuccess(mBinding.rvNumCardList, numCardListInfoList);
                        } else {
                            showToast(mActivity, result.getErrorCode());
                            afterRefreshFailed(mBinding.rvNumCardList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        afterRefreshFailed(mBinding.rvNumCardList);
                    }
                });
    }


    private void showSearchDialog() {
        if (mSearchDialog == null) {
            mSearchDialog = new SearchDialog(mActivity);
            final EditText searchEt = (EditText) mSearchDialog.findViewById(R.id.search_et);
            final ImageView deleteTextIv = (ImageView) mSearchDialog.findViewById(R.id.delete_text_iv);
            final TextView typeTv = (TextView) mSearchDialog.findViewById(R.id.type_tv);
            TextView tv_search = (TextView) mSearchDialog.findViewById(R.id.search_tv);
            iv_noData = (ImageView) mSearchDialog.findViewById(R.id.noneDataIv);
            rl_progress = (RelativeLayout) mSearchDialog.findViewById(R.id.dialog_collect_search_progress);
            mSearchRecyclerView = (RecyclerView) mSearchDialog.findViewById(recyclerView);

            mSearchRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
            if (Constants.intent_type_service == mSearchType) {
                typeTv.setText("服务");
                searchEt.setHint("请输入服务名称");
                serviceAdapter = new SearchServiceAdapter(mActivity);
                mSearchRecyclerView.setAdapter(serviceAdapter);
            } else {
                typeTv.setText("商品");
                searchEt.setHint("请输入商品名称");
                goodsAdapter = new SearchGoodsAdapter(mActivity);
                mSearchRecyclerView.setAdapter(goodsAdapter);
            }

            searchEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable == null) return;
                    String s = editable.toString();
                    if (!StringUtils.isEmptyOrWhitespace(s)) {

                        if (Constants.intent_type_goods == mSearchType) {
                            getSearchGoodsHttp(s);
                        } else {
                            getSearchServiceHttp(s);
                        }
                        search_key = s;

                        deleteTextIv.setVisibility(View.VISIBLE);
                    } else {
                        deleteTextIv.setVisibility(View.GONE);
                    }
                }
            });

            deleteTextIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchEt.setText("");
                    if (goodsAdapter != null) {
                        goodsAdapter.clearData();
                    }
                    if (serviceAdapter != null) {
                        serviceAdapter.clearData();
                    }
                }
            });

            typeTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constants.intent_type_goods == mSearchType) {
                        typeTv.setText("服务");
                        searchEt.setHint("请输入服务名称");
                        mSearchType = Constants.intent_type_service;
                        serviceAdapter = new SearchServiceAdapter(mActivity);
                        mSearchRecyclerView.setAdapter(serviceAdapter);
                    } else {
                        typeTv.setText("商品");
                        searchEt.setHint("请输入商品名称");
                        mSearchType = Constants.intent_type_goods;
                        goodsAdapter = new SearchGoodsAdapter(mActivity);
                        mSearchRecyclerView.setAdapter(goodsAdapter);
                    }
                }
            });

            tv_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constants.intent_type_goods == mSearchType) {
                        if (search_key != null && !search_key.isEmpty()) {
                            getSearchGoodsHttp(search_key);
                        } else {
                            showToast(mActivity, "请输入商品名称");
                        }
                    } else {
                        if (search_key != null && !search_key.isEmpty()) {
                            getSearchServiceHttp(search_key);
                        } else {
                            showToast(mActivity, "请输入服务名称");
                        }
                    }
                }
            });

            mSearchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    searchEt.setText("");
                    if (goodsAdapter != null) {
                        goodsAdapter.clearData();
                    }
                    if (serviceAdapter != null) {
                        serviceAdapter.clearData();
                    }
                }
            });

        }

        mSearchDialog.show();


    }

    /**
     * 服务搜索
     *
     * @param s
     */
    private void getSearchServiceHttp(String s) {
        rl_progress.setVisibility(View.VISIBLE);
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("name", s);
        params.put("limit", "1000");
        mServiceListClient.reqApi("queryServiceList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ServiceInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ServiceInfo> result) {
                        rl_progress.setVisibility(View.GONE);
                        if (result.isSuccess()) {
                            List<ServiceInfo> data = result.getData();
                            if (serviceAdapter != null) {
                                serviceAdapter.setList(data);
                                if (data != null) {
                                    iv_noData.setVisibility(View.GONE);
                                } else {
                                    iv_noData.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            mActivity.showToast(result.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        rl_progress.setVisibility(View.GONE);
                    }
                });

    }

    /**
     * 商品搜索
     *
     * @param s
     */
    private void getSearchGoodsHttp(String s) {
        rl_progress.setVisibility(View.VISIBLE);
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("name", s);
        params.put("limit", "1000");
        mGoodsListClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        rl_progress.setVisibility(View.GONE);
                        if (result.isSuccess()) {
                            List<GoodInfo> mList = result.getData();
                            if (goodsAdapter != null) {
                                goodsAdapter.setList(mList);
                                if (mList.size() != 0) {
                                    iv_noData.setVisibility(View.GONE);
                                } else {
                                    iv_noData.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        rl_progress.setVisibility(View.GONE);
                        mActivity.showToast("网络异常");
                    }
                });

    }

    @SuppressLint("NewApi")
    private void registerBus() {


        if (rxManager == null) rxManager = new RxManager();

        rxManager.on(Constants.bus_type_info, new Action1<Object>() {
            @Override
            public void call(Object o) {
                if (mSearchDialog != null) {
                    mSearchDialog.dismiss();
                }
                insertShopToCar(o);
            }
        });

        rxManager.on(Constants.bus_db, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (Constants.type_update_commitOrder_db == integer) {
                    vipInfo = null;
                    vipType.set(3);
                    carAdapter.setList(commitOrderTempInfos);
                    updateBottomMoney();
                }
            }
        });

        rxManager.on(Constants.type_vip_info, new Action1<VipInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void call(VipInfo info) {
                if (info != null) {
                    vipInfo = info;
                    mBinding.tvCollectVipName.setVisibility(View.VISIBLE);
                    mBinding.tvCollectVipPhone.setVisibility(View.VISIBLE);
                    mBinding.tvCollectVipMoney.setVisibility(View.VISIBLE);
                    mBinding.tvCollectVipName.setText("会员姓名: " + vipInfo.getName());
                    mBinding.tvCollectVipPhone.setText("会员电话: " + vipInfo.getPhone());
                    if (!StringUtils.isEmpty(vipInfo.getProductList())) {
                        vipType.set(2);
                        mBinding.tvCollectVipCardName.setText("卡片名称: " + vipInfo.getTypeName());
                        mBinding.tvCollectVipCardType.setText("卡片类型: 次卡");
                        String residueNum = getResidueNum();
                        mBinding.tvCollectVipMoney.setText("剩余套餐: " + residueNum);
                    } else {
                        vipType.set(1);
                        mBinding.tvCollectVipCardName.setText("卡片名称: " + vipInfo.getTypeName());
                        mBinding.tvCollectVipCardType.setText("卡片类型: 折扣卡");
                        mBinding.tvCollectVipMoney.setText("剩余金额: " + vipInfo.getMoney() + "元");
                    }
                }
            }
        });
        //支付完成之后刷新界面
        rxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (integer == Constants.type_order_pay_finish) {
                    //清空购物车
                    clearTempDb();
                    updateBottomMoney();
                    carAdapter.deleteAll();
                    vipType.set(3);
                    vipInfo = null;
                    vipCardInfo = null;
                    orderNums.clear();

                    startHttp = 0;
                    loadHttpData();
                    //loadServiceHttpData();
                }
            }
        });
    }

    /**
     * 按返回键提示清空购物车
     */
    public void clearAllOrder() {
        if (serviceNum != 0 || goodNum != 0) {
            mBackDialog = new CustomDialog(mActivity);
            mBackDialog.setContent("返回后所购物品将不会保留,确定返回?");
            mBackDialog.setCancelVisiable();
            mBackDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetOrder();

                }
            });
            if (!mActivity.isFinishing()) {
                mBackDialog.show();
            }
        } else {
            mActivity.finish();
        }
    }

    /**
     * 点击会员按钮
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void selectVipUser() {
        if (commitOrderTempInfos.size() > 0 && commitOrderTempInfos.get(0).getOrderType() == 1 && commitOrderTempInfos.get(0).getVipUserId() > 0) {
            showLongToast(mActivity, "购物车内含有会员取单商品或服务,不可选取其他会员");
        } else {
            Intent intent = new Intent(mActivity, SearchActivity.class);
            intent.putExtra(Constants.intent_search_type, Constants.type_addAppointment_vip);
            intent.putExtra(Constants.intent_flag, 0);
            mActivity.startActivity(intent);
        }

    }

    /**
     * 点击非会员按钮
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void selectNormalUser() {
        vipCardInfo = null;
        vipInfo = null;
        vipType.set(3);
    }


    public void selectNumCardVip() {
        if (commitOrderTempInfos.size() > 0 && commitOrderTempInfos.get(0).getOrderType() == 1 && commitOrderTempInfos.get(0).getVipUserId() > 0) {
            showLongToast(mActivity, "购物车内含有会员取单商品或服务,不可选取其他次卡");
        } else {
            Intent intent = new Intent(mActivity, SearchActivity.class);
            intent.putExtra(Constants.intent_search_type, Constants.type_addAppointment_vip);
            intent.putExtra(Constants.intent_flag, 1);
            if (judgeHaveNumCard()) {
                intent.putExtra(Constants.intent_vip_card_no, numCardID);
            }
            mActivity.startActivity(intent);
        }
    }

    /**
     * 点击纯折扣卡按钮
     */
    public void selectOnlyDiscount() {
        if (commitOrderTempInfos.size() > 0 && commitOrderTempInfos.get(0).getOrderType() == 1 && commitOrderTempInfos.get(0).getVipUserId() > 0) {
            showLongToast(mActivity, "购物车内含有会员取单商品或服务,不可选取其他折扣卡");
        } else {
            cardSelectDialog = new CardSelectDialog(mActivity, mActivity, 2);
            cardSelectDialog.setCardSelectListener(new CardSelectDialog.CardSelectListenner() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onCardSelect(VipCardInfo vipCardInfo) {
                    vipInfo = null;
                    CollectMoneyViewModel.this.vipCardInfo = vipCardInfo;
                    vipType.set(4);
                    mBinding.tvCollectVipCardName.setText("卡片名称: " + vipCardInfo.getName());
                    mBinding.tvCollectVipCardType.setText("卡片类型: 折扣卡");
                    mBinding.tvCollectVipName.setVisibility(View.GONE);
                    mBinding.tvCollectVipPhone.setVisibility(View.GONE);
                    mBinding.tvCollectVipMoney.setVisibility(View.GONE);
                }
            });
            cardSelectDialog.show();
        }
    }

    public void clickButton(View v) {
        mIsGuadan = false;
        if (commitOrderTempInfos.size() != 0) {
            commitBuyHttp();
        } else {
            mActivity.showToast("请添加商品或服务");
        }

    }


    public void clicGuadanButton(View v) {
        mIsGuadan = true;
        if (commitOrderTempInfos.size() != 0) {
            commitBuyHttp();
        } else {
            mActivity.showToast("请添加商品或服务");
        }

    }


    /**
     * 点击挂单页面
     *
     * @param v
     */
    public void clickOrder(View v) {
        mActivity.turnToActivity(PayListActivity.class);
    }

    private void commitBuyHttp() {
        params.clear();

        params.put("allList", new Gson().toJson(commitOrderTempInfos));
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        String username = mBinding.etCollectNormalName.getText().toString();
        params.put("userName", username == null ? "" : username);
        String phone = mBinding.etCollectNormalPhone.getText().toString();
        params.put("userPhone", phone == null ? "" : phone);

        //isClassification   -1是非会员,0是会员,1是次卡,2是纯折扣
        if (judgeHaveNumCard()) {
            if (commitOrderTempInfos.get(0).getOrderType() == 0) {//如果非取单
                if (vipInfo == null || StringUtils.isEmpty(vipInfo.getProductList())) {
                    showToast(mActivity, "请选择次卡会员");
                    return;
                }
                if (vipInfo.getType() != numCardID) {
                    showToast(mActivity, "请选择对应的次卡会员");
                    return;
                }
                boolean isOver = getIsOverNum();
                if (isOver) return;
                params.put("vipUserId", vipInfo.getId() + "");
            } else {//如果是取单
                params.put("vipUserId", commitOrderTempInfos.get(0).getVipUserId() + "");
            }
            params.put("isClassification", "1");
            commitOrder();
        } else {
            if (vipInfo != null) {
                if (!StringUtils.isEmpty(vipInfo.getProductList())) {//如果是次卡
                    showToast(mActivity, "购物车无次卡服务,不能选择次卡下单");
                    return;
                }
                params.put("vipUserId", vipInfo.getId() + "");
                selectVipType();
            } else {
                if (vipCardInfo != null) {//如果是折扣卡
                    params.put("cardId", vipCardInfo.getId() + "");
                    params.put("isClassification", "2");
                    commitOrder();
                } else if (commitOrderTempInfos.get(0).getVipUserId() != 0) {
                    params.put("vipUserId", commitOrderTempInfos.get(0).getVipUserId() + "");
                    selectVipType();
                } else {
                    params.put("isClassification", "-1");
                    commitOrder();
                }

            }
        }


    }

    private void commitOrder() {
        mActivity.showRDialog();
        mBinding.okBtn.setEnabled(false);
        mClient.reqApi("insertOrderForPad", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mBinding.okBtn.setEnabled(true);
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {

                            if (mIsGuadan) {
                                //清空购物车
                                clearTempDb();
                                updateBottomMoney();
                                carAdapter.deleteAll();
                                vipType.set(3);
                                vipInfo = null;
                                vipCardInfo = null;
                                orderNums.clear();
                                mActivity.showToast("挂单成功");
                            } else {
                                Intent intent = new Intent(mActivity, CommitOrderActivity.class);
                                intent.putExtra(Constants.intent_id, result.getData());
                                intent.putExtra("userName", nonMemberName);
                                intent.putExtra("userPhone", nonMemberPhone);
                                mActivity.startActivity(intent);
                            }

                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                        mBinding.okBtn.setEnabled(true);
                        mActivity.showToast("网络异常");
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void clearTempDb() {
        commitOrderTempInfos.clear();
        serviceNum = 0;
        goodNum = 0;
        totalAmt = 0.00;
        mBinding.moneyTv.setText(AppUtil.formatStandardMoney(totalAmt) + " 元");
        mBinding.bvUnm.setText(serviceNum + goodNum + "");
        mBinding.bvUnm.setVisibility(View.GONE);
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
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("barCode", barCode);
        mActivity.showRDialog();
        mGoodsClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        mActivity.hideRDialog();
                        List<GoodInfo> data = result.getData();
                        if (result.isSuccess()) {
                            if (data.size() > 0) {
                                GoodInfo goodInfo = data.get(0);
                                insertShopToCar(goodInfo);
                            } else {
                                showTurnToAddActivityDialog();
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

    /**
     * 跳转到添加商品的页面
     */
    private void showTurnToAddActivityDialog() {
        if (mCustomDialog == null) {
            mCustomDialog = new CustomDialog(mActivity);
            mCustomDialog.setContent("找不到该商品,是否添加?");
            mCustomDialog.setCancelVisiable();
            mCustomDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.turnToActivity(AddGoodsActivity.class);
                    mCustomDialog.dismiss();
                }
            });
        }
        mCustomDialog.show();
    }

    //判断是否可以购买   限制条件,如果购物车内有次卡服务,则不能买其他产品,如果购物车内有其他产品,则不能购买该次卡服务
    private boolean judgeHaveNumCard() {
        boolean b = false;
        for (CommitOrderTempInfo info : commitOrderTempInfos) {
            if (info.getType() == 3) {
                numCardID = info.getCardID();
                b = true;
                break;
            }
        }
        return b;
    }

    //判断是否可以购买   限制条件,如果购物车内有次卡服务,则不能买其他产品,如果购物车内有其他产品,则不能购买该次卡服务
    private boolean judgeCanBuyNumService(int cardID) {
        boolean result;
        if (commitOrderTempInfos.size() == 0) {
            result = true;
        } else {
            CommitOrderTempInfo info = new CommitOrderTempInfo();
            info.setCardID(cardID);
            info.setType(3);
            if (commitOrderTempInfos.contains(info)) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    //次卡会员剩余套餐
    private String getResidueNum() {
        try {
            String[] productNum = vipInfo.getProductNum().split(",");
            String[] productName = vipInfo.getProductName().split(",");
            StringBuilder builder = new StringBuilder();
            for (int i = 1; i < productNum.length; i++) {
                int productItemNum = Integer.parseInt(productNum[i]);
                builder.append(productName[i] + " " + productItemNum + "次  ");
            }
            return builder.toString();
        } catch (Exception e) {
        }
        return "";
    }

    //是否超出当前套餐
    private boolean getIsOverNum() {
        try {
            String[] productNum = vipInfo.getProductNum().split(",");
            String[] productList = vipInfo.getProductList().split(",");
            StringBuilder builder = new StringBuilder();
            builder.append("该次卡会员只能购买\n");
            boolean isOver = false;
            for (int i = 1; i < productNum.length; i++) {
                int productItemNum = Integer.parseInt(productNum[i]);
                int productItemId = Integer.parseInt(productList[i]);
                List<CommitOrderTempInfo> list = new ArrayList<>();
                for (CommitOrderTempInfo info : commitOrderTempInfos) {
                    if (info.getId() == productItemId) {
                        list.add(info);
                    }
                }
                if (list.size() > 0) {
                    int commitItemNum = 0;
                    for (CommitOrderTempInfo info : list) {
                        commitItemNum += info.getNum();
                    }
                    if (commitItemNum > productItemNum) {
                        builder.append(productItemNum + "次" + list.get(0).getName() + "服务\n");
                        isOver = true;
                    }
                }
            }
            if (isOver) {
                showLongToast(mActivity, builder.toString());
            }
            return isOver;
        } catch (Exception e) {
        }
        return true;
    }

    public void showCardList() {
        showCard.set(true);
    }


    //更新将取单数据恢复
    private void resetOrder() {
        commitOrderTempInfos.clear();
        mBackDialog.dismiss();
        if (orderNums.size() > 0) {
            StringBuilder orders = new StringBuilder();
            for (int i = 0; i < orderNums.size(); i++) {
                if (i < orderNums.size() - 1) {
                    orders.append(orderNums.get(i) + ",");
                } else {
                    orders.append(orderNums.get(i));
                }
            }
            params.clear();
            params.put("branchId", SpUtil.getBranchId() + "");
            params.put("orderId", orders.toString());
            params.put("type", "0");
            mClient.reqApi("updateOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                    .updateUI(new Callback<BaseResult>() {
                        @Override
                        public void onSuccess(BaseResult info) {
                            if (info.isSuccess()) {
                            } else {
                                if (mActivity != null) mActivity.showToast(info.getErrorMessage());
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                        }
                    });
        } else {

        }
        mActivity.finish();
    }

    private SelectDialog mSelectDialog;
    private String vipPayType;

    //会员区别用会员价还是折扣价
    private void selectVipType() {
        if (mSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("会员价", "4"));
            genderInfos.add(new SelectInfo("折扣价", "0"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            mSelectDialog = new SelectDialog(mActivity, adapter);

            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    vipPayType = genderInfos.get(position).getType();
                    params.put("isClassification", vipPayType);
                    commitOrder();

//                    obAnimalGenderType.set(genderInfo.getType());
//                    obAnimalGender.set(genderInfo.getName());
                }
            });
        }
        mSelectDialog.show();
    }


    private boolean isAccordance(CommitOrderTempInfo info, CommitOrderTempInfo target) {
        boolean result = false;
        if (info.getPromotionAmt() != 0) {
            result = (target.getId() == info.getId()) && target.getPromotionAmt() == info.getPromotionAmt() && target.getType() == info.getType() && target.getIsGift() == info.getIsGift() && target.getUserId() == info.getUserId();
        } else if (info.getPrice() != 0) {
            result = (target.getId() == info.getId()) && target.getPrice() == info.getPrice() && target.getType() == info.getType() && target.getIsGift() == info.getIsGift() && target.getUserId() == info.getUserId();
        } else if (info.getCardID() != 0) {
            result = (target.getId() == info.getId()) && target.getType() == info.getType() && target.getCardID() == info.getCardID() && target.getUserId() == info.getUserId();
        }
        return result;
    }

    public void reset() {
        mActivity = null;
        if (rxManager != null) {
            rxManager.clear();
            rxManager = null;
        }
        if (mGoodsClient != null) {
            mGoodsClient.reset();
            mGoodsClient = null;
        }
        if (mInsertOrderClient != null) {
            mInsertOrderClient.reset();
            mInsertOrderClient = null;
        }
        if (mGoodsListClient != null) {
            mGoodsListClient.reset();
            mGoodsListClient = null;
        }
        if (mServiceListClient != null) {
            mServiceListClient.reset();
            mServiceListClient = null;
        }
        if (mVipClient != null) {
            mVipClient.reset();
            mVipClient = null;
        }
        if (mListClient != null) {
            mListClient.reset();
            mListClient = null;
        }
        mBackDialog = null;
        mSearchDialog = null;
        goodsAdapter = null;
        serviceAdapter = null;
        if (null != goodTypeInfos && mLastPosition != -1 && goodTypeInfos.size() > mLastPosition) {
            goodTypeInfos.get(mLastPosition).setSelect(0);
        }
        if (serviceTypeInfos != null && serviceLastPostion != -1 && serviceTypeInfos.size() > serviceLastPostion) {
            serviceTypeInfos.get(serviceLastPostion).setSelect(0);
        }
        cardSelectDialog = null;
    }
}
