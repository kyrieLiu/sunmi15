package ys.app.pad.itemmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.view.View;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.RequestDialog;


/**
 * Created by liuyin on 2017/9/13.
 */

public class AppointmentItemModel extends BaseObservable {
    private AppointmentBean model;
    public ObservableBoolean isWaiting=new ObservableBoolean(false);
    public ObservableBoolean isFailer=new ObservableBoolean(false);
    public ObservableBoolean isAready=new ObservableBoolean(false);
    private Context fragment;

    private CustomDialog confirmDialog;
    private CustomDialog noTimeDialog;
    private CustomDialog repeatDialog;
    private ApiClient<BaseResult> mClient;
    private RxManager rxManager;

    public AppointmentItemModel(AppointmentBean model, Context fragment, boolean isAppointmentAready, boolean isAppointmentWait, boolean isAppointmentFailer) {
        this.model = model;
        this.fragment=fragment;
        isWaiting.set(isAppointmentWait);
        isFailer.set(isAppointmentFailer);
        isAready.set(isAppointmentAready);
        if (mClient==null){
            mClient=new ApiClient<>();
        }
    }

    @Bindable
    public AppointmentBean getModel() {
        return model;
    }

    public void setModel(AppointmentBean model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }

    /**
     * 确定预约
     */
    public void showConfirmDialog(){
        if(confirmDialog == null){
            confirmDialog = new CustomDialog(fragment);
            confirmDialog.setContent("确定预约吗?");
            confirmDialog.setCancelVisiable();
            confirmDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmDialog.dismiss();
                    switchAppointmentState(1);
                }
            });

        }
        confirmDialog.show();
    }

    /**
     * 暂无时间
     */
    public void noTimeDialog(){
        if(noTimeDialog == null){
            noTimeDialog = new CustomDialog(fragment);
            noTimeDialog.setContent("是否提交?");
            noTimeDialog.setCancelVisiable();
            noTimeDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noTimeDialog.dismiss();
                    switchAppointmentState(4);
                }
            });
        }
        noTimeDialog.show();
    }

    /**
     * 重新预约
     */
    public void repeatDialog(){
        if(repeatDialog == null){
            repeatDialog = new CustomDialog(fragment);
            repeatDialog.setContent("是否重新预约?");
            repeatDialog.setCancelVisiable();
            repeatDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    repeatDialog.dismiss();
                }
            });

        }
        repeatDialog.show();
    }
    private void switchAppointmentState(final int state){
        final Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("id", model.getId()+"");
        params.put("state", state+"");


        showRDialog();
        mClient.reqApi("updateBespeak", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (rxManager==null){
                            rxManager=new RxManager();
                        }
                        rxManager.post(Constants.bus_type_http_result,Constants.type_addAppointment_success);
                        hideRDialog();
                        String toast="";
                        if (state==1){
                            toast="预约成功";
                        }else{
                            toast="已驳回预约";
                        }
                        Toast.makeText(fragment,toast,Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onError(Throwable e) {
                        // updatePayResult(info);
                        super.onError(e);
                        hideRDialog();
                    }
                });
    }
    private RequestDialog mRDialog;
    public void showRDialog() {
        if(mRDialog == null){
            mRDialog = new RequestDialog(fragment);
        }
        mRDialog.show();
    }
    public void hideRDialog() {
        if(mRDialog != null){
            mRDialog.hide();
        }
    }

}
