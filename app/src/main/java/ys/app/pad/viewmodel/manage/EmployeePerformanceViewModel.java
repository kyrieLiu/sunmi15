package ys.app.pad.viewmodel.manage;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableDouble;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.EmployeePerformanceActivity;
import ys.app.pad.adapter.manage.EmployeePerformance1Adapter;
import ys.app.pad.adapter.manage.EmployeePerformance2Adapter;
import ys.app.pad.adapter.manage.EmployeePerformance3Adapter;
import ys.app.pad.databinding.ActivityEmployeePerformanceBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseDataResult;
import ys.app.pad.model.EmployeePerformanceInfo;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.NetWorkUtil;

/**
 * Created by aaa on 2017/5/2.
 */
public class EmployeePerformanceViewModel extends BaseActivityViewModel {

    public ObservableBoolean obIsGoodsSuccess = new ObservableBoolean(false);
    public ObservableBoolean obIsServiceSuccess = new ObservableBoolean(false);
    public ObservableBoolean obIsChargeSuccess = new ObservableBoolean(false);
    public ObservableDouble obGoodsTotalMoney = new ObservableDouble(0.00);
    public ObservableDouble obServiceTotalMoney = new ObservableDouble(0.00);
    public ObservableDouble obChargeTotalMoney = new ObservableDouble(0.00);
    private EmployeePerformanceActivity mActivity;
    private ActivityEmployeePerformanceBinding mBinding;
    private ApiClient<BaseDataResult<EmployeePerformanceInfo>> mClient;
    private long mEmployeeId;
    private EmployeePerformance1Adapter mEmployeePerformance1Adapter;
    private EmployeePerformance2Adapter mEmployeePerformance2Adapter;
    private EmployeePerformance3Adapter mEmployeePerformance3Adapter;

    public EmployeePerformanceViewModel(EmployeePerformanceActivity activity, ActivityEmployeePerformanceBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<BaseDataResult<EmployeePerformanceInfo>>();
        init();
    }

    public void setEmployeeIdFormIntent(long employeeId) {
        this.mEmployeeId = employeeId;
        loadHttpData();
    }


    private void init() {
        mBinding.recyclerView1.setLayoutManager(new LinearLayoutManager(mActivity));
        mEmployeePerformance1Adapter = new EmployeePerformance1Adapter(mActivity);


        mBinding.recyclerView2.setLayoutManager(new LinearLayoutManager(mActivity));
        mEmployeePerformance2Adapter = new EmployeePerformance2Adapter(mActivity);


        mBinding.recyclerView3.setLayoutManager(new LinearLayoutManager(mActivity));
        mEmployeePerformance3Adapter = new EmployeePerformance3Adapter(mActivity);


        mBinding.recyclerView2.setAdapter(mEmployeePerformance1Adapter);
        mBinding.recyclerView1.setAdapter(mEmployeePerformance2Adapter);
        mBinding.recyclerView3.setAdapter(mEmployeePerformance3Adapter);
    }


    @Override
    public void reloadData(View view) {
        loadHttpData();
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            isNetWorkErrorVisible.set(true);
        }
    }


    private void getDataList() {
        isLoadingVisible.set(true);
        Map<String, String> params = new HashMap<>();
        params.put("userId", mEmployeeId + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("beginTime", DateUtil.longFormatDate(new Date().getTime()));
//        params.put("endTime", "2017-05-02");
        mClient.reqApi("queryEmployeePerformance", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseDataResult<EmployeePerformanceInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResult<EmployeePerformanceInfo> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            setData(result.getData());
                        } else {
                            isNetWorkErrorVisible.set(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoadingVisible.set(false);
                        isNetWorkErrorVisible.set(true);
                        super.onError(e);
                    }
                });
    }


    private void setData(EmployeePerformanceInfo data) {
        if (data == null) {
            isNetWorkErrorVisible.set(true);
            return;
        }
        List<EmployeePerformanceInfo.OrderDetailedListProductBean> serviceList = data.getOrderDetailedListProduct();
        List<EmployeePerformanceInfo.OrderDetailedListCommodityBean> goodsList = data.getOrderDetailedListCommodity();
        List<EmployeePerformanceInfo.RechargeListBean> rechargeList = data.getRechargeList();

        if (goodsList == null || goodsList.size() == 0) {
            obIsGoodsSuccess.set(false);
        } else {
            obIsGoodsSuccess.set(true);
            mEmployeePerformance1Adapter.setList(goodsList);

            EmployeePerformanceInfo.OrderDetailedListCommodityBean bean = null;
            BigDecimal totalMoney = new BigDecimal(0);
            for (int i = 0; i < goodsList.size(); i++) {
                bean = goodsList.get(i);
                totalMoney=add(totalMoney,bean.getRealMoney());
            }
            obGoodsTotalMoney.set(totalMoney.doubleValue());
        }

        if (serviceList == null || serviceList.size() == 0) {
            obIsServiceSuccess.set(false);
        } else {
            obIsServiceSuccess.set(true);
            mEmployeePerformance2Adapter.setList(serviceList);

            EmployeePerformanceInfo.OrderDetailedListProductBean bean = null;
            BigDecimal totalMoney = new BigDecimal(0);
            for (int i = 0; i < serviceList.size(); i++) {
                bean = serviceList.get(i);
                totalMoney=add(totalMoney,bean.getRealMoney());
            }
            obServiceTotalMoney.set(totalMoney.doubleValue());
        }

        if (rechargeList == null || rechargeList.size() == 0) {
            obIsChargeSuccess.set(false);
        } else {
            obIsChargeSuccess.set(true);
            mEmployeePerformance3Adapter.setList(rechargeList);

            EmployeePerformanceInfo.RechargeListBean bean = null;
            BigDecimal totalMoney =new BigDecimal(0);
            for (int i = 0; i < rechargeList.size(); i++) {
                bean = rechargeList.get(i);
                totalMoney=add(totalMoney,bean.getRechargeAmt());
            }
            obChargeTotalMoney.set(totalMoney.doubleValue());
        }
    }
    public BigDecimal add(BigDecimal b1, double v2) {
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

}
