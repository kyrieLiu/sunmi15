package ys.app.pad.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.CheckActivity;
import ys.app.pad.adapter.CheckAdapter;
import ys.app.pad.databinding.ActivityCheckBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.DeleteDialog;

/**
 * Created by aaa on 2017/3/7.
 */

public class CheckViewModel extends BaseActivityViewModel {
    private final CheckActivity mActivity;
    private final ActivityCheckBinding mBinding;
    private final ApiClient<BaseListResult<GoodInfo>> mClient;
    private final ApiClient<BaseResult> mCommitClient;
    public ObservableBoolean clickEnable=new ObservableBoolean(false);
    private CheckAdapter mAdapter;
    private View mContentView;
    private Map<String, String> params;
    private int scanCount = 0;//标识第几次扫码

    private DeleteDialog mConfirmDeleteDialog;

    public CheckViewModel(CheckActivity activity, ActivityCheckBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
        this.mCommitClient = new ApiClient<>();
        this.params = new HashMap<>();
    }

    public void init() {

        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.activity_check_header, null);
        mBinding.recyclerView.addHeaderView(mContentView);
        mAdapter = new CheckAdapter(mActivity);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, R.drawable.shape_recyclerview_division_line));

        mBinding.recyclerView.setIAdapter(mAdapter);
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
            isNetWorkErrorVisible.set(true);
        }
    }

    public void getDataList() {
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("limit", "2000");
        isLoadingVisible.set(true);
        mClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            List<GoodInfo> mList = result.getData();
                            for (GoodInfo info:mList){
                                info.setInventoryNum(info.getStockNum());
                            }
                            mAdapter.setList(mList);
                            if (mList.size()>0){
                                clickEnable.set(true);
                                isNoneDataVisible.set(false);
                            }else{
                                mActivity.showToast("暂无商品可盘点");
                                isNoneDataVisible.set(true);
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.showToast("网络异常");
                        isLoadingVisible.set(false);
                        isNetWorkErrorVisible.set(true);
                    }
                });
    }


    public void clickButton(View v) {
        showDialog();

    }

    private void showDialog() {

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
                    commitCheckData();
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

    private void commitCheckData(){
        params.clear();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        List<GoodInfo> list=mAdapter.getList();
        String checkData=new Gson().toJson(list);
        Log.i("info","盘点数据=="+checkData);
        params.put("inventoryList", checkData);
        clickEnable.set(false);
        mActivity.showRDialog();
        mCommitClient.reqLongTimeApi("checkInvertory", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            mActivity.showToast("盘点成功,请点击盘点记录查看");
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                            clickEnable.set(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                        clickEnable.set(true);
                        mActivity.showToast("网络超时,请检查网络");
                    }
                });
    }


    /**
     * 将扫描的条形码传入进行查询
     */
    public void setScanResult(String info) {
        params.clear();
        params.put("branchId", SpUtil.getBranchId() + "");
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("barCode", info);
        isLoadingVisible.set(true);
        mClient.reqApi("getGoods", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<GoodInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<GoodInfo> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            List<GoodInfo> mList = result.getData();
                            if (mList.size()>0){
                                isNoneDataVisible.set(false);
                                mAdapter.setScanList(mList, scanCount);
                                scanCount++;
                            }else{
                                isNoneDataVisible.set(true);
                            }

                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.showToast("网络异常");
                        isLoadingVisible.set(false);
                        isNetWorkErrorVisible.set(true);
                    }
                });
    }
}
