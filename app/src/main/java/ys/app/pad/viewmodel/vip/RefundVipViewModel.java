package ys.app.pad.viewmodel.vip;

import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.AppApplication;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.vip.RefundVipActivity;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.databinding.ActivityRefundVipBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseDataResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.RefundVipInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.DeleteDialog;
import ys.app.pad.widget.dialog.SelectDialog;


/**
 * Created by liuyin on 2017/12/10.
 */

public class RefundVipViewModel extends BaseActivityViewModel {
    private RefundVipActivity mActivity;
    private ApiClient<BaseDataResult<RefundVipInfo>> mClient;
    public ObservableField<String> employeeName=new ObservableField<>();
    public ObservableField<String> refundMoney=new ObservableField<>();
    public ObservableField<String> presentAndRealMoney=new ObservableField<>();
    public ObservableField<String> realMoney=new ObservableField<>();
    public ObservableField<String> nowMoney=new ObservableField<>();
    public ObservableField<String> resultMoney=new ObservableField<>();

    private long vipUserId;
    private SelectInfo selectInfo;
    private RxManager mRxManager;
    private SelectDialog employeeSelectDialog;
    private ActivityRefundVipBinding binding;


    public RefundVipViewModel(RefundVipActivity activity,  long vipUserId,ActivityRefundVipBinding binding) {
        this.mActivity = activity;
        this.vipUserId=vipUserId;
        this.binding=binding;
        this.mClient = new ApiClient<>();
        init();
    }

    @Override
    public void reloadData(View view) {
        loadHttpData();
    }

    public void init() {
        loadHttpData();
    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
            showToast(mActivity, Constants.network_error_warn);
        }
    }


    public void getDataList() {
        Map<String, String> parmas = new HashMap<>();
        parmas.put("branchId", SpUtil.getBranchId()+"");
        parmas.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        parmas.put("vipUserId",vipUserId+"");
        mActivity.showRDialog();
        isNoneDataVisible.set(true);
        mClient.reqApi("refundVipUser", parmas, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseDataResult<RefundVipInfo>>() {
                    @Override
                    public void onSuccess(BaseDataResult<RefundVipInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            RefundVipInfo info=result.getData();
                            resultMoney.set(String.valueOf(info.getRefundAmt())+" 元");
                            presentAndRealMoney.set(info.getRealAmt()+" + "+info.getPresentAmt());
                            nowMoney.set(String.valueOf(info.getNowAmt()));
                            realMoney.set(String.valueOf(info.getRealAmt()));
                            isNoneDataVisible.set(false);
                        } else {
                            resultMoney.set("退款金额");
                            presentAndRealMoney.set("实际金额 + 赠送金额");
                            nowMoney.set("剩余金额");
                            realMoney.set("实际金额");
                            refundMoney.set("0");
                            binding.tvRefundZero.setVisibility(View.VISIBLE);
                            isNoneDataVisible.set(true);
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

    public void selectEmployee(){
        if (employeeSelectDialog == null) {
            List<EmployeeInfo> mEmployeeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().loadAll();
            final List<SelectInfo> genderInfos = new ArrayList<>();
            if (mEmployeeInfos != null && !mEmployeeInfos.isEmpty()) {
                SelectInfo genderInfo;
                for (EmployeeInfo info : mEmployeeInfos) {//添加种类
                    genderInfo = new SelectInfo(info.getName(), String.valueOf(info.getId()));
                    genderInfos.add(genderInfo);
                }
            }
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            employeeSelectDialog = new SelectDialog(mActivity, adapter);
            employeeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    selectInfo = genderInfos.get(position);
                    employeeName.set(selectInfo.getName());
                }
            });
        }
        employeeSelectDialog.show();
    }
    private CustomDialog confirmDialog;
    private DeleteDialog verifyDialog;
    public void clickRefund(){
        if (confirmDialog == null) {
            confirmDialog = new CustomDialog(mActivity);

            confirmDialog.setContent("确定退卡吗?");


            confirmDialog.setCancelVisiable();
        }

        confirmDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog.dismiss();
                if (verifyDialog == null) {
                    verifyDialog = new DeleteDialog(mActivity);
                }
                verifyDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
                    @Override
                    public void verificationPwd(boolean b) {
                        if (b) {
                            if (verifyDialog != null) {
                                verifyDialog.dismiss();
                            }
                            commitData();

                        } else {
                            Toast.makeText(mActivity, "密码输入错误请重新输入", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDismiss() {
                        verifyDialog = null;
                    }
                });
                verifyDialog.show();

            }

        });
        confirmDialog.show();
    }

    private void commitData(){
        if (selectInfo==null){
            showToast(mActivity,"请选择退款员工");
            return;
        }
        if (StringUtils.isEmpty(refundMoney.get())){
            showToast(mActivity,"请输入退款金额");
            return;
        }
        Map<String, String> parmas = new HashMap<>();
        parmas.put("branchId", SpUtil.getBranchId()+"");
        parmas.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        parmas.put("shopId",SpUtil.getShopId()+"");
        parmas.put("vipUserId", vipUserId+"");
        parmas.put("userId", selectInfo.getType());
        parmas.put("userName", selectInfo.getName()+"");
        parmas.put("money", refundMoney.get());

        ApiClient<BaseResult> client=new ApiClient();
        mActivity.showRDialog();
        client.reqApi("refundVipUserOrder", parmas, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if(mRxManager == null){
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result,Constants.type_deleteVipCard_success);
                            showToast(mActivity,"退卡成功");
                            mActivity.finish();
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

    public void clear(){
        employeeSelectDialog=null;
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
        confirmDialog=null;
        verifyDialog=null;
    }
}
