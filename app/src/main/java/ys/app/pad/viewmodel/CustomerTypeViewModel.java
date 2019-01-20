package ys.app.pad.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.adapter.ConfirmCheckOutAdapter;
import ys.app.pad.databinding.DialogCustomerTypeBinding;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.VipInfo;
import ys.app.pad.nfc.MagneticCardActivity;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomerTypeDialog;

/**
 * Created by aaa on 2017/3/15.
 */

public class CustomerTypeViewModel extends BaseActivityViewModel {
    private final Activity mActivity;
    private final DialogCustomerTypeBinding mBinding;
    private final CustomerTypeDialog mDialog;
    public int mPosition;
    public ObservableField<String> mVipPhone = new ObservableField<>();
    public ObservableField<String> mVipCard = new ObservableField<>();
    public ObservableField<String> mName = new ObservableField<>();
    public ObservableField<String> mPhone = new ObservableField<>();
    public ObservableField<String> mAnimalName = new ObservableField<>();
    public ObservableInt obPosition = new ObservableInt();
    private ConfirmCheckOutAdapter mConfirmCheckOutAdapter;
    private final ApiClient<BaseListResult<VipInfo>> mVipListClient;
    private boolean isGuadan;


    public CustomerTypeViewModel(CustomerTypeDialog dialog, Activity activity, DialogCustomerTypeBinding binding,boolean isGuadan) {
        this.mActivity = activity;
        this.mDialog = dialog;
        this.mBinding = binding;
        this.mVipListClient = new ApiClient<>();
        this.isGuadan=isGuadan;
        obPosition.set(0);
        init();
    }

    private void init() {
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("会员"));
        mBinding.tabLayout.addTab(mBinding.tabLayout.newTab().setText("非会员"));

        mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPosition = tab.getPosition();
                obPosition.set(mPosition);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setVipListener();
    }
    private void setVipListener(){
        mBinding.vipPhoneEt.addTextChangedListener(new TextWatcher() {
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
                    getSearchVipHttp(s);
                } else {
                    if (mConfirmCheckOutAdapter != null){
                        mConfirmCheckOutAdapter.clearData();
                    }
                }
            }
        });


        mBinding.rlDialogCollectVip.setLayoutManager(new LinearLayoutManager(mActivity));

        mConfirmCheckOutAdapter = new ConfirmCheckOutAdapter(mActivity,isGuadan);
        mBinding.rlDialogCollectVip.setAdapter(mConfirmCheckOutAdapter);
    }

    /**
     * 会员搜索
     *
     * @param s
     */
    private void getSearchVipHttp(String s) {
        Map<String, String> parmas = new HashMap<>();
        //parmas.put("branchId",SpUtil.getBranchId()+"");
        parmas.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
//        parmas.put("phone", s);
        parmas.put("name", s);
        mVipListClient.reqApi("queryVip", parmas, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<VipInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipInfo> result) {
                        if (result.isSuccess()) {
                            List<VipInfo> mList = result.getData();
                            if (mConfirmCheckOutAdapter != null && mList != null && !mList.isEmpty()) {
                                mConfirmCheckOutAdapter.setList(mList);
                            }
                        } else {
                            Toast.makeText(mActivity,result.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(mActivity,"网络异常",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    /**
     * 跳过
     *
     * @param v
     */
    public void clickJump(View v) {
        mDialog.dismiss();
        mDialog.onClickJump();
    }

    /**
     * 确定
     *
     * @param v
     */
    public void clickButton(View v) {
        //Toast.makeText(mActivity,"点击啦",Toast.LENGTH_SHORT).show();

        if (0 == mPosition) {
            mDialog.onClickButton(mPosition, null, mVipPhone.get(), mVipCard.get());
//            mDialog.onClickButton(mPosition, null, mVipPhone.get(), mVipCard.get());
        } else {
            mDialog.onClickButton(mPosition, mName.get(), mPhone.get(), mAnimalName.get());
        }

    }

    public void searchCardNo(){
        Intent intent=new Intent(mActivity, MagneticCardActivity.class);
        intent.putExtra(Constants.intent_flag,"shouyin");
        mActivity.startActivity(intent);
    }
}
