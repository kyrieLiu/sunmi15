package ys.app.pad.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.adapter.ModifyTransactionAdapter;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.DeleteDialog;
import ys.app.pad.widget.dialog.RequestDialog;
import ys.app.pad.widget.dialog.SelectDialog;

/**
 * Created by admin on 2017/6/11.
 */

public class ItemTransactionDetailViewModel extends BaseObservable {
    private OrderInfo.OrderDetailedListBean model;
    private Context mContext;
    private List<EmployeeInfo> mEmployeeInfos;
    private ModifyTransactionAdapter mModifyTransactionAdapter;
    private SelectDialog mTypeSelectDialog;
    private EmployeeInfo mEmployeeInfo;
    private OrderInfo mOrderInfo;
    private ApiClient<BaseResult> mApiClient;
    public ObservableField<String> userName = new ObservableField<>("");
    public ObservableField<String> price = new ObservableField<>("");
    private RxManager rxManager;
    private CustomDialog mDeleteDialog;
    private DeleteDialog mConfirmDeleteDialog;
    private int position;
    public ObservableBoolean canRefund=new ObservableBoolean();

    public ItemTransactionDetailViewModel(OrderInfo.OrderDetailedListBean model, Context context, OrderInfo info,int position) {
        setModel(model, info,position);
        this.mContext = context;
    }


    @Bindable
    public OrderInfo.OrderDetailedListBean getModel() {
        return model;
    }

    public void setModel(OrderInfo.OrderDetailedListBean model, OrderInfo info,int position) {
        this.model = model;
        mOrderInfo = info;
        this.position=position;
        userName.set(model.getUserName());
        if (model.getIsRefund()==0&&model.getIsClassification()!=1&&model.getIsGift()==0&&model.getType()==1){//次卡.赠品和已经退款过的订单不支持退款
            canRefund.set(true);
        }else{
            canRefund.set(false);
        }
        if (model.getPrice()<0){
           price.set("￥"+AppUtil.formatStandardMoney(0-model.getPrice()));
        }else{
            price.set("￥"+AppUtil.formatStandardMoney(model.getPrice()));
        }
        notifyPropertyChanged(BR.model);
    }

    public void modifyClick() {
        mEmployeeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().loadAll();
        if (mModifyTransactionAdapter == null) {
            if (mEmployeeInfos != null) {
                for (EmployeeInfo info : mEmployeeInfos) {
                    if (info.isSelect()) {
                        info.setSelect(false);
                    }
                }
            }
            mModifyTransactionAdapter = new ModifyTransactionAdapter((Activity) mContext, mEmployeeInfos);
            mTypeSelectDialog = new SelectDialog((Activity) mContext, mModifyTransactionAdapter);
            mTypeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    mEmployeeInfo = mEmployeeInfos.get(position);
                    for (int i = 0; i < mEmployeeInfos.size() - 1; i++) {
                        if (position != i) {
                            mEmployeeInfos.get(i).setSelect(false);
                        }
                    }
                    if (mOrderInfo != null) {
                        String orderInfo = mOrderInfo.getOrderInfo();
                        if (!TextUtils.isEmpty(orderInfo)) {
                            if (orderInfo.contains("充值")) {//updateRecharge
                                updateRecharge();
                            } else {
                                updateOrderDetailed();
                            }
                        } else {//updateOrderDetailed
                            updateOrderDetailed();
                        }
                    }

                }

                private void updateRecharge() {
                    if (mApiClient == null) {
                        mApiClient = new ApiClient<BaseResult>();
                    }
                    Map<String, String> parmars = new HashMap<String, String>();
                    parmars.put("shopId", model.getShopId() + "");
                    parmars.put("branchId", SpUtil.getBranchId() + "");
                    parmars.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
                    parmars.put("id", model.getId() + "");
                    parmars.put("userId", mEmployeeInfo.getId() + "");
                    mApiClient.reqApi("updateRecharge", parmars, ApiRequest.RespDataType.RESPONSE_JSON)
                            .updateUI(new Callback<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult baseResult) {
                                    if (baseResult.isSuccess()) {
                                        userName.set(mEmployeeInfo.getName() + "");
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, baseResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }
                            });
                }

                private void updateOrderDetailed() {
                    if (mApiClient == null) {
                        mApiClient = new ApiClient<BaseResult>();
                    }

                    Map<String, String> parmars = new HashMap<String, String>();
                    parmars.put("shopId", model.getShopId() + "");
                    parmars.put("branchId", SpUtil.getBranchId() + "");
                    parmars.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
                    parmars.put("id", model.getId() + "");
                    parmars.put("userId", mEmployeeInfo.getId() + "");
                    parmars.put("userName", mEmployeeInfo.getName());
                    mApiClient.reqApi("updateOrderDetailed", parmars, ApiRequest.RespDataType.RESPONSE_JSON)
                            .updateUI(new Callback<BaseResult>() {
                                @Override
                                public void onSuccess(BaseResult baseResult) {
                                    if (baseResult.isSuccess()) {
                                        userName.set(mEmployeeInfo.getName() + "");
                                        Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
                                        if (rxManager == null) {
                                            rxManager = new RxManager();
                                        }
                                        rxManager.post(Constants.type_today_achivement, 0);
                                    } else {
                                        Toast.makeText(mContext, baseResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                }
                            });
                }
            });
        } else {
            mTypeSelectDialog.setModifyTransactionData(mEmployeeInfos);
        }

        mTypeSelectDialog.show();
    }

    /**
     * 校验店长密码
     */
    public void showConfirmDialog() {
        if (mContext == null) {
            return;
        }
        if (mConfirmDeleteDialog == null){
            mConfirmDeleteDialog = new DeleteDialog(mContext);
        }
        mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
            @Override
            public void verificationPwd(boolean b) {
                if (b) {
                    if (mConfirmDeleteDialog != null) {
                        mConfirmDeleteDialog.dismiss();
                    }
                    modifyClick();
                } else {
                    Toast.makeText(mContext, "密码输入错误请重新输入", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onDismiss() {
                mConfirmDeleteDialog = null;
            }
        });
        mConfirmDeleteDialog.show();
    }

    /**
     *单件退款
     */
    public void refoundMoney(){
        if (mDeleteDialog == null) {
            mDeleteDialog = new CustomDialog(mContext);
            if (model.getVipUserId()>0){
                mDeleteDialog.setContent("确定退款?");
            }else{
                mDeleteDialog.setContent("需要向顾客线下支付,确定退款?");
            }
            mDeleteDialog.setCancelVisiable();
        }

        mDeleteDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteDialog.dismiss();
                if (mConfirmDeleteDialog == null) {
                    mConfirmDeleteDialog = new DeleteDialog(mContext);
                }
                mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
                    @Override
                    public void verificationPwd(boolean b) {
                        if (b) {
                            mConfirmDeleteDialog.dismiss();
                            updateRefound();
                        } else {
                            Toast.makeText(mContext,"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDismiss() {
                        mConfirmDeleteDialog = null;
                    }
                });
                mConfirmDeleteDialog.show();

            }

        });
        mDeleteDialog.show();
    }

    /**
     * 单笔退款
     */
    private void updateRefound() {
        if (mApiClient == null) {
            mApiClient = new ApiClient<BaseResult>();
        }
        Map<String, String> params = new HashMap<String, String>();
        params.put("shopId",SpUtil.getShopId());
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("orderNo",mOrderInfo.getOrderNo());
        params.put("cashierTradeNo","");
        params.put("orderDetailedId",model.getId()+"");
        params.put("operator",model.getUserName());
        showRDialog();
        mApiClient.reqApi("refundOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        hideRDialog();
                        if (baseResult.isSuccess()) {
                            if (rxManager == null) {
                                rxManager = new RxManager();
                            }//通知订单详情移除该商品
                            rxManager.post(Constants.type_today_achivement_detail, position);
                            rxManager.post(Constants.type_today_achivement,0);
                        } else {
                            Toast.makeText(mContext,baseResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideRDialog();
                    }
                });
    }


    private RequestDialog mRDialog;
    public void showRDialog() {
        if (mRDialog == null) {
            mRDialog = new RequestDialog(mContext);
        }
            mRDialog.show();

    }

    public void hideRDialog() {
        if (mRDialog != null) {
                mRDialog.hide();
                mRDialog.dismiss();
        }
    }


    public void reset() {
        mTypeSelectDialog = null;
        mEmployeeInfos = null;
        mModifyTransactionAdapter = null;
        mEmployeeInfos = null;
        mContext = null;
        if (mApiClient != null) {
            mApiClient.reset();
            mApiClient = null;
        }
    }
}
