package ys.app.pad.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.CommitOrderTempInfo;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;

/**
 * Created by aaa on 2017/4/26.
 */

public class Order2ItemViewModel extends BaseObservable {
    private  int mPosition;
    private OrderInfo model;
    private BaseActivity mActivity;
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> vipPrice = new ObservableField<>();
    private CustomDialog mDialog;
    private ApiClient<BaseResult> mClient;
    private RxManager mRxManager;
    private CustomDialog customDialog;

    public Order2ItemViewModel(int position, OrderInfo model, BaseActivity activity) {
        this.mPosition = position;
        this.model = model;
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
    }


    @Bindable
    public OrderInfo getModel() {
        return model;
    }

    public void setModel(OrderInfo model,int mPosition) {
        this.mPosition=mPosition;
        this.model = model;

        notifyPropertyChanged(BR.model);
    }

    public void clickOpenOrClose(View v) {
        model.setExpand(!model.isExpand());
        if (model.getIsClick() == -1) {
            model.setClick(0);
        } else if (model.getIsClick() == 0) {
            model.setClick(1);
        } else if (model.getIsClick() == 1) {
            model.setClick(0);
        }
    }

    public void clickPay(View v) {

        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("orderId", model.getOrderNo());
        params.put("type", "4");
        mActivity.showRDialog();
        mClient.reqApi("updateOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult info) {
                        mActivity.hideRDialog();
                        if (info.isSuccess()) {
                            CollectMoneyViewModel.orderNums.add(model.getOrderNo());
                                getOrder();

                        } else {
                            mActivity.showToast(info.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });


    }

    /**
     * 删除订单
     *
     * @param v
     */
    public void clickDeleteButton(View v) {
        showDeleteDialog();
    }

    private void showDeleteDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog(mActivity);
            mDialog.setContent("确认删除吗?");
            mDialog.setCancelVisiable();
            mDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDialog.dismiss();
                    deleteHttp();
                }
            });

        }
        mDialog.show();
    }


    private void deleteHttp() {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("orderId", model.getOrderNo());
        params.put("type", "4");

        mClient.reqApi("updateOrder", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult info) {
                        mActivity.hideRDialog();
                        if (info.isSuccess()) {
                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_delete_position, mPosition);
                        } else {
                            mActivity.showToast(info.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivity.hideRDialog();
            if (mRxManager == null) mRxManager = new RxManager();

            mRxManager.post(Constants.bus_db, Constants.type_update_commitOrder_db);
            mActivity.finish();
        }
    };
    /**
     * 取单
     */
    private void getOrder(){
        if (model.getVipUserId()>0){
            if (CollectMoneyViewModel.commitOrderTempInfos.size()>0&&CollectMoneyViewModel.commitOrderTempInfos.get(0).getVipUserId()==model.getVipUserId()){
                mActivity.showRDialog();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        insertVipOrderShopCar();
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else{
                insertNormalCar();
            }
        }else{
            insertNormalCar();
        }

    }
    //订单为会员有多个订单时候需要合并
    private void insertVipOrderShopCar(){

        List<OrderInfo.OrderDetailedListBean> orderDetailedList=model.getOrderDetailedList();

        for (OrderInfo.OrderDetailedListBean bean:orderDetailedList){
            int type=bean.getType(),isPromotion=bean.getIsPromotion(),id=bean.getProductId(),userId=bean.getUserId();

            CommitOrderTempInfo orderInfo=new CommitOrderTempInfo();
            orderInfo.setId(id);orderInfo.setUserId(userId);orderInfo.setCardID(model.getCardId());
            if (bean.getIsClassification() ==0) {
                orderInfo.setType(type);
                if (Constants.is_promotion == isPromotion) {
                    orderInfo.setPromotionAmt(bean.getPromotionPrice());
                } else {
                    orderInfo.setPrice(bean.getPrice());
                }
            } else {
                orderInfo.setType(3);
            }
            CommitOrderTempInfo entity = null;
            if (CollectMoneyViewModel.commitOrderTempInfos.contains(orderInfo)) {
                for (int i=0;i<CollectMoneyViewModel.commitOrderTempInfos.size();i++){
                    entity=CollectMoneyViewModel.commitOrderTempInfos.get(i);
                    boolean accordance=isAccordance(entity,orderInfo);
                    if (accordance)break;
                }
                entity.setNum(entity.getNum() +bean.getCount());
            } else {
                 entity=creatEntity(bean);
                CollectMoneyViewModel.commitOrderTempInfos.add(entity);
            }
        }
    }
    //订单为普通订单或者该会员的第一个订单
    private void insertNormalCar(){
        List<CommitOrderTempInfo> list=CollectMoneyViewModel.commitOrderTempInfos;
        if (list.size()>0){
            if (customDialog==null){
                customDialog = new CustomDialog(mActivity);
                customDialog.setContent("购物车内已有商品,确定清空购物车取单?");
            }
            TextView mCancelTv = (TextView)customDialog.findViewById(R.id.cancel_tv);
            mCancelTv.setVisibility(View.VISIBLE);
            mCancelTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                    mActivity.hideRDialog();
                }
            });
            customDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customDialog.dismiss();
                    CollectMoneyViewModel.commitOrderTempInfos.clear();
                    insertDataToShopCar();
                }
            });
            customDialog.show();
        }else{
            insertDataToShopCar();
        }
    }
    private void insertDataToShopCar(){
        mActivity.showRDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
            List<OrderInfo.OrderDetailedListBean> orderDetailedList=model.getOrderDetailedList();
            for (OrderInfo.OrderDetailedListBean bean:orderDetailedList){
                CommitOrderTempInfo entity=creatEntity(bean);
                CollectMoneyViewModel.commitOrderTempInfos.add(entity);
            }
            handler.sendEmptyMessage(0);
            }
        }).start();
    }

    /**
     * 创建需要存储的对象
     */
    private CommitOrderTempInfo creatEntity(OrderInfo.OrderDetailedListBean bean){
        CommitOrderTempInfo entity = new CommitOrderTempInfo();
        entity.setId(bean.getProductId());
        entity.setUserId(bean.getUserId());
        entity.setUserName(bean.getUserName());
        entity.setNum(bean.getCount());
        //0:折扣卡 1:次卡 2:纯折扣卡 3:生日折扣卡
        if (bean.getIsClassification()!=1){
            entity.setType(bean.getType());
        }else{
            entity.setType(3);
        }
        entity.setName(bean.getName());
        entity.setVipUserId(model.getVipUserId());
        entity.setIsPromotion(bean.getIsPromotion());
        if (Constants.is_promotion == bean.getIsPromotion()) {
            entity.setPromotionAmt(bean.getPromotionPrice());
            entity.setPrice(bean.getPrice());
        }else{
            entity.setCardID(model.getCardId());
            entity.setPrice(bean.getPrice());
        }
        entity.setPrice(bean.getPrice());
        entity.setIsGift(bean.getIsGift());
        entity.setOrderType(1);//取单
        return entity;
    }
    private boolean isAccordance(CommitOrderTempInfo info,CommitOrderTempInfo target){
        boolean result=false;
        if (info.getPromotionAmt()!=0){
            result=(target.getId()==info.getId())&&target.getPromotionAmt()==info.getPromotionAmt()&&target.getType()==info.getType()&&target.getIsGift()==info.getIsGift()&&target.getUserId()==info.getUserId();
        }else if (info.getPrice()!=0){
            result=(target.getId()==info.getId())&&target.getPrice()==info.getPrice()&&target.getType()==info.getType()&&target.getIsGift()==info.getIsGift()&&target.getUserId()==info.getUserId();
        }else if (info.getCardID()!=0){
            result=(target.getId()==info.getId())&&target.getType()==info.getType()&&target.getCardID()==info.getCardID()&&target.getUserId()==info.getUserId();
        }
        return result;
    }
}
