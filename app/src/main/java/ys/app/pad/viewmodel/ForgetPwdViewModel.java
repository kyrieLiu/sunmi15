package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.ForgetPwdActivity;
import ys.app.pad.activity.manage.ModifyPswActivity;
import ys.app.pad.databinding.ForgetPwdActivityBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseResult;
import ys.app.pad.utils.Logger;
import ys.app.pad.utils.StringUtils;

/**
 * Created by admin on 2017/6/10.
 */

public class ForgetPwdViewModel extends BaseActivityViewModel {
    private final ForgetPwdActivity mActivity;
    private final ForgetPwdActivityBinding mBinding;
    private final ApiClient<BaseResult> mClient;
    private  ApiClient<BaseResult> messageClient;
    public ObservableField<String> smsCode = new ObservableField<>();
    public ObservableField<String> time = new ObservableField<>("发送短信");
    public final int COUNT = 60;
    public ObservableBoolean canSend=new ObservableBoolean(false);
    private String mSmsCode;
    private String phoneNum;
    private String shopID;

    public ForgetPwdViewModel(ForgetPwdActivity activity, ForgetPwdActivityBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();

        initPhoneInfo();
    }

    private void initPhoneInfo(){

        Map<String, String> params = new HashMap<>();
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            shopID = (String) get.invoke(c, "ro.serialno");
            params.put("equipmentId", (String) get.invoke(c, "ro.serialno"));//设备序列号
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("shopId", shopID);
        mActivity.showRDialog();
        mClient.reqApi("selectEquipmentShop", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            phoneNum=result.getData();
                            mBinding.tvForgetPhone.setText("接收验证码手机号: "+phoneNum);
                            canSend.set(true);
                        } else {
                            mActivity.showToast(result.getErrorCode());
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });
    }

    public void clickOkBtn(View view) {

        if (StringUtils.isEmpty(smsCode.get())){
            showToast(mActivity,"请填写验证码");
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("phoneCode", smsCode.get());
        params.put("shopId",shopID);
        mActivity.showRDialog();
        mClient.reqApi("lostLoginPsw", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            Intent intent=new Intent(mActivity, ModifyPswActivity.class);
                            intent.putExtra(Constants.intent_flag,1);
                            mActivity.startActivity(intent);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorCode());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        mActivity.showToast("加载异常,请检查网络");
                        super.onError(e);
                    }
                });
    }

    public void sendSms() {
        //倒计时
        Subscription mSubscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(COUNT + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return COUNT - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        canSend.set(false);//在发送数据的时候设置为不能点击
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        canSend.set(true);
                        time.set("发送短信");
                    }

                    @Override
                    public void onError(Throwable e) {
                       canSend.set(true);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        time.set(aLong + "s后重发");
                    }
                });

        getSmsHttp();
    }

    private void getSmsHttp() {

        if (messageClient==null)messageClient=new ApiClient<>();
        Map<String, String> params = new HashMap<>();
        params.put("type", "1");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("shopId", shopID);
        messageClient.reqApi("sendSms", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult info) {
                        mActivity.hideRDialog();
                        if (info.isSuccess()) {
                            mSmsCode = info.getData();
                            mActivity.showToast("短信已发送至店铺负责人手机号码上，请注意查收");

                            Logger.e("短信验证码--" + mSmsCode);
                        } else {
                            mActivity.showToast(info.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        mActivity.showToast("发送失败,请检查网络");
                        super.onError(e);
                    }
                });
    }
}
