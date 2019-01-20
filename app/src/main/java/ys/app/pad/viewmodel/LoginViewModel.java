package ys.app.pad.viewmodel;

import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;

import com.greendao.LoginInfoDao;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.MainActivity;
import ys.app.pad.activity.ForgetPwdActivity;
import ys.app.pad.activity.LoginActivity;
import ys.app.pad.databinding.ActivityLoginBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.LoginInfo;
import ys.app.pad.shangmi.screen.utils.MainConnectUtils;
import ys.app.pad.utils.SpUtil;

/**
 * Created by lyy on 2017/2/20 10:26.
 * email：2898049851@qq.com
 */
public class LoginViewModel extends BaseActivityViewModel {

    private LoginActivity mActivity;
    private ApiClient<BaseListResult<LoginInfo>> mClient;
    private String psw;
    private String user;
    private ActivityLoginBinding binding;
    private MainConnectUtils utils;

    public LoginViewModel(LoginActivity activity, ActivityLoginBinding binding) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        this.binding = binding;

        binding.tvTitle.setText(SpUtil.getShopName());
        utils = MainConnectUtils.getInstance().setContext(activity);
    }

    private void init() {
        LinearLayout root_r1 = binding.rootRl;
        LinearLayout root_r2 = binding.rootR2;

        DisplayMetrics dm=new DisplayMetrics();
        dm=mActivity.getResources().getDisplayMetrics();
        int r1_width = dm.widthPixels;
        int r1_height =dm.heightPixels;

        int r2_width = (int)(519.0f/1920.0f * (float)r1_width);
        int r2_height = (int)((557.0f/1041.0f) * (float)r1_height);

        int r2_margin_left = (int)(320.0f/1920.0f * (float)r1_width);
        int r2_margin_top = (int)(147.0f/1920.0f * (float)r1_width);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(r2_width,r2_height);
        params.setMargins(r2_margin_left,r2_margin_top,0,0);

        root_r2.setLayoutParams(params);
    }

    public void clickButton(View v) {
        login();
    }


    public void login() {
        Map<String, String> params = new HashMap<>();
        try {
            Class c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            params.put("equipmentId", (String) get.invoke(c, "ro.serialno"));//设备序列号
            //params.put("equipmentId", "TM18185N00154");
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("passWord", psw);
        mActivity.showRDialog();

        mClient.reqApi("userLogin", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<LoginInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<LoginInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            List<LoginInfo> data = result.getData();
                            if (data == null || data.size() == 0) {
                                mActivity.showToast("登录失败，请稍后重试");
                            } else {
                                LoginInfo loginInfo = data.get(0);
                                LoginInfoDao loginInfoDao = GreenDaoUtils.getmDaoSession().getLoginInfoDao();
                                loginInfoDao.deleteAll();
                                loginInfoDao.insert(loginInfo);
                                SpUtil.setIsLogin(true);
                                SpUtil.setShopName(loginInfo.getShopName());
                                SpUtil.setShopId(loginInfo.getEquipmentId());
                                SpUtil.setBranchId(loginInfo.getBranchId());
                                SpUtil.setHeadOfficeId(loginInfo.getHeadOfficeId());
                                SpUtil.setSandMchNo(loginInfo.getMchNo());
                                SpUtil.setSandKey(loginInfo.getMd5Key());
                                SpUtil.setVersionModule(loginInfo.getIsModular());
                                toNextActivity();
                            }

                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        mActivity.showToast("网络异常,请检查网络重试");
                        super.onError(e);
                    }
                });
    }

    private void toNextActivity() {

        utils.saveShopInformation();
        mActivity.startActivity(new Intent(mActivity, MainActivity.class));
        mActivity.finish();

    }


    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void destroy() {
        mClient.reset();
        mClient = null;
        mActivity = null;
    }

    public void forgetPwdClick() {
        turnToActivity(mActivity, ForgetPwdActivity.class);
    }


}
