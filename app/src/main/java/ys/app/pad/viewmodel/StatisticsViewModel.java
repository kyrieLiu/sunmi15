package ys.app.pad.viewmodel;

import android.databinding.ObservableField;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.shangmi.printer.BlueToothPrintUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.StatisticsActivity;
import ys.app.pad.adapter.PayMethodStatisticsAdapter;
import ys.app.pad.databinding.ActivityStatisticsBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.itemmodel.DailySettlementBean;
import ys.app.pad.model.BaseDataResult;
import ys.app.pad.model.StatisticsDataInfo;
import ys.app.pad.shangmi.printer.AidlUtil;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.DateUtil;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.PieChart;

/**
 * Created by aaa on 2017/6/7.
 */
public class StatisticsViewModel extends BaseActivityViewModel {
    private StatisticsActivity mActivity;
    private ActivityStatisticsBinding mBinding;
    private ApiClient<BaseDataResult<StatisticsDataInfo>> mClient;
    private ApiClient<BaseDataResult<DailySettlementBean>> orderClient;
    private PayMethodStatisticsAdapter adapter2;
    public ObservableField<String> obTotalAmt = new ObservableField<>();


    public StatisticsViewModel(StatisticsActivity activity, ActivityStatisticsBinding mBinding) {
        this.mActivity = activity;
        this.mBinding = mBinding;
        this.mClient = new ApiClient<>();
        orderClient=new ApiClient<>();
        loadHttpData();
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataHttp();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
        }
    }

    private void getDataHttp() {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("branchId",SpUtil.getBranchId()+"");
        parmas.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        parmas.put("beginTime", DateUtil.longFormatDate(new Date().getTime()));
        mActivity.showRDialog();
        mClient.reqApi("queryStatistics", parmas, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseDataResult<StatisticsDataInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResult<StatisticsDataInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            StatisticsDataInfo data = result.getData();
                            StatisticsDataInfo.ProductTypeListBean bean = new StatisticsDataInfo.ProductTypeListBean();
                            bean.setTotalAmt(data.getRealAmt());
                            bean.setName("商品");
                            data.getProductTypeList().add(0, bean);

                            StatisticsDataInfo.ProductTypeListBean chargeBean=new StatisticsDataInfo.ProductTypeListBean();
                            chargeBean.setName("充值");
                            chargeBean.setTotalAmt(data.getRechargeAmt());
                            data.getProductTypeList().add(chargeBean);
                            setData(data);
                        } else {
                            mActivity.showToast(result.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                        Toast.makeText(mActivity,"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void setData(StatisticsDataInfo data) {

        PieChart pieChart=mBinding.pcStaticToday;
        pieChart.setData(data.getProductTypeList());


        List<StatisticsDataInfo.ProductTypeListBean> productTypeList = data.getProductTypeList();

        List<StatisticsDataInfo.PayMethodListBean> payMethodListBeanList=data.getPayMethodList();
        adapter2 = new PayMethodStatisticsAdapter(mActivity, payMethodListBeanList);
        mBinding.recyclerView2.setLayoutManager(new GridLayoutManager(mActivity, 6));
        mBinding.recyclerView2.setAdapter(adapter2);

        BigDecimal totalMoney=new BigDecimal(0);
        if (productTypeList != null && !productTypeList.isEmpty()) {
            for (StatisticsDataInfo.ProductTypeListBean bean : productTypeList) {
                totalMoney=add(totalMoney,bean.getTotalAmt());
            }
        }
        obTotalAmt.set(AppUtil.formatStandardMoney(totalMoney.doubleValue()) + " 元");



    }
    public BigDecimal add(BigDecimal b1, double v2) {
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    public void printOrder(final TextView tv_print){
        Map<String, String> parmas = new HashMap<>();
        parmas.put("branchId",SpUtil.getBranchId()+"");
        parmas.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        mActivity.showRDialog();
        tv_print.setEnabled(false);
        orderClient.reqApi("selectSettlementByDay", parmas, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseDataResult<DailySettlementBean>>() {
                    @Override
                    public void onSuccess(BaseDataResult<DailySettlementBean> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            tv_print.setEnabled(true);
                            if ("D1".equals(Build.MODEL)){
                                BlueToothPrintUtil.getInstance().printDailySettlement(result.getData());
                            }else{
                                AidlUtil.getInstance().printDailySettlement(result.getData());
                            }

                        } else {
                            tv_print.setEnabled(true);
                            mActivity.showToast(result.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                        Toast.makeText(mActivity,"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void clear(){
        mClient.reset();
        mClient=null;
        orderClient.reset();
        orderClient=null;
    }
}
