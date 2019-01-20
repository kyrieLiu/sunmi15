package ys.app.pad.viewmodel.vip;

import android.content.Intent;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BR;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.AddAnimalActivity;
import ys.app.pad.activity.vip.AddVipActivity;
import ys.app.pad.activity.vip.ChargeActivity;
import ys.app.pad.activity.vip.RefundVipActivity;
import ys.app.pad.activity.vip.VipDetailActivity;
import ys.app.pad.activity.vip.VipRecordActivity;
import ys.app.pad.adapter.AnimalAdapter;
import ys.app.pad.adapter.vip.VipDetailMenuAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ActivityVipDetailBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.VipInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.wrapperRecylerView.SpaceItemDecoration;

/**
 * Created by aaa on 2017/3/1.
 */

public class VipDetailViewModel extends BaseActivityViewModel {

    private final VipDetailActivity mActivity;
    private final ApiClient<BaseListResult<AnimalInfo>> mClient;
    private final ActivityVipDetailBinding mBinding;
    @Bindable
    public VipInfo mInfo;
    private AnimalAdapter mAdapter;
    private RxManager mRxManager;
    public ObservableField<String> money = new ObservableField<>();
    public ObservableBoolean isAnimal = new ObservableBoolean();
    private ApiClient<BaseListResult<VipInfo>> mVipApiClient;
    private int flag;

    public VipDetailViewModel(VipDetailActivity activity, ActivityVipDetailBinding binding, int flag) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        this.mVipApiClient = new ApiClient<>();
        this.mBinding = binding;
        this.flag = flag;
        registerBus();

    }

    private void getMoney(VipInfo mInfo) {
        this.mInfo = mInfo;
        if (flag == 0) {
            money.set(AppUtil.formatStandardMoney(mInfo.getMoney()) + "元");
            if (mInfo.getIsPetBirthdayDiscount()==1){
                mBinding.llVipDetailBirthday.setVisibility(View.VISIBLE);
                mBinding.tvVipDetailBirthdayName.setText(mInfo.getPetBirthdayDiscountName());
            }
        } else {
            mBinding.tvVipDetailType.setText("剩余套餐:  ");
            try {
                String[] productNum = mInfo.getProductNum().split(",");
                String[] productName = mInfo.getProductName().split(",");
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < productNum.length; i++) {
                    int productItemNum = Integer.parseInt(productNum[i]);
                    builder.append(productName[i] + " " + productItemNum + "次  ");

                }
                money.set(builder.toString());
            } catch (Exception e) {
            }

        }

    }

    private void registerBus() {
        if (mRxManager == null) {
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (Constants.type_updateAnimal_success == integer) {
                    getAnimalHttp();
                } else if (Constants.type_charge_finish == integer || Constants.type_updateVip_success == integer) {
                    refreshDetail();
                } else if (Constants.type_deleteVipCard_success == integer) {
                    mActivity.finish();
                }
            }
        });

    }

    private void refreshDetail() {
        Map<String, String> params = new HashMap<>();
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("cardNo", mInfo.getCardNo());
        params.put("classification", flag + "");
        mVipApiClient.reqApi("selectVip", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<VipInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipInfo> vipInfoBaseListResult) {
                        List<VipInfo> data = vipInfoBaseListResult.getData();
                        if (data != null && !data.isEmpty()) {
                            for (VipInfo info : data) {
                                Log.i("orderInfo", "加载会员信息  " + mInfo.getCardNo() + "   后期==" + info.getCardNo());
                                if (mInfo.getCardNo().equals(info.getCardNo())) {
                                    mBinding.tvVipDetailName.setText(info.getName());
                                    mBinding.tvVipDetailPhone.setText(info.getPhone());
                                    mBinding.tvVipDetailCardNO.setText(info.getCardNo());
                                    if (flag == 0) {
                                        mBinding.tvVipcardDetailType.setText(info.getTypeName());
                                        money.set(AppUtil.formatStandardMoney(info.getMoney()));
                                        if (info.getIsPetBirthdayDiscount()==1){
                                            mBinding.llVipDetailBirthday.setVisibility(View.VISIBLE);
                                            mBinding.tvVipDetailBirthdayName.setText(info.getPetBirthdayDiscountName());
                                        }else{
                                            mBinding.llVipDetailBirthday.setVisibility(View.GONE);
                                        }
                                    } else {
                                        getMoney(info);
                                    }

                                    break;
                                }
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }


    public void init() {
        mAdapter = new AnimalAdapter(mActivity);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity,2));
        mBinding.recyclerView.addItemDecoration(new SpaceItemDecoration(10));
        mBinding.recyclerView.setAdapter(mAdapter);

        VipDetailMenuAdapter menuAdapter=new VipDetailMenuAdapter(mActivity);
        mBinding.rlVipDetailMenu.setLayoutManager(new GridLayoutManager(mActivity,2));
        mBinding.rlVipDetailMenu.setAdapter(menuAdapter);
        menuAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        clickVipRecharge();
                        break;
                    case 1:
                        clickAddAnimal();
                        break;
                    case 2:
                        clickModifyButton();
                        break;
                    case 3:
                        clickCharge();
                        break;
                    case 4:
                        clickRefundMoney();
                        break;



                }
            }
        });
    }

    public void setData(VipInfo info) {
        if (info != null) {
            getMoney(info);
            getAnimalHttp();
        }
    }

    private void getAnimalHttp() {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("userId", mInfo.getId() + "");
        params.put("classification", flag + "");
        mClient.reqApi("queryAnimal", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AnimalInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<AnimalInfo> result) {
                        mActivity.hideRDialog();
                        List<AnimalInfo> data = result.getData();
                        if (result.isSuccess()) {
                            if (data == null || data.isEmpty()) {
                                isAnimal.set(false);
                            } else {
                                isAnimal.set(true);
                                mAdapter.setList(result.getData());
                            }
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                            isAnimal.set(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        isAnimal.set(false);
                        super.onError(e);
                    }
                });
    }

    @Bindable
    public VipInfo getmInfo() {
        return mInfo;
    }

    public void setmInfo(VipInfo mInfo) {
        this.mInfo = mInfo;
        notifyPropertyChanged(BR.mInfo);
    }

    public void clickAddAnimal() {
        Intent intent = new Intent(mActivity, AddAnimalActivity.class);
        intent.putExtra(Constants.intent_info, mInfo);
        mActivity.startActivity(intent);
    }

    public void clickModifyButton() {
        Intent intent = new Intent(mActivity, AddVipActivity.class);
        intent.putExtra(Constants.intent_type, Constants.intent_modify);
        intent.putExtra(Constants.intent_info, mInfo);
        intent.putExtra(Constants.intent_flag, flag);
        mActivity.startActivity(intent);
    }

    public void clickCharge() {
        if (mInfo.getCardState() == 3) {
            Toast.makeText(mActivity, "该会员卡已作废,不可充值", Toast.LENGTH_SHORT).show();
            return;
        }
        if (flag == 1) {
            boolean canRecharge = getCanRecharge();
            if (!canRecharge) return;
        }
        Intent intent = new Intent(mActivity, ChargeActivity.class);
        intent.putExtra(Constants.intent_id, mInfo.getId());
        intent.putExtra(Constants.intent_info, mInfo);
        mActivity.startActivity(intent);
    }

    public void clickVipRecharge() {
        Intent intent = new Intent(mActivity, VipRecordActivity.class);
        intent.putExtra(Constants.intent_vip_shop_id, mInfo.getShopId());
        intent.putExtra(Constants.intent_vip_user_id, mInfo.getId());
        mActivity.startActivity(intent);
    }

    public void clickRefundMoney() {
        if (flag == 1) {
           showToast(mActivity,"次卡不可退卡");
           return;
        }
        Intent intent = new Intent(mActivity, RefundVipActivity.class);
        intent.putExtra("vipUserId", mInfo.getId());
        mActivity.startActivity(intent);
    }

    private boolean getCanRecharge() {
        try {
            String[] productNum = mInfo.getProductNum().split(",");
            for (int i = 1; i < productNum.length; i++) {

                int productItemNum = Integer.parseInt(productNum[i]);
                if (productItemNum > 0) {
                    showToast(mActivity, "该卡内还有套餐,请用完再充值");
                    return false;
                }
            }
        } catch (Exception e) {
        }
        return true;
    }

    public void clear() {
        if (mRxManager != null) {
            mRxManager.clear();
            mRxManager = null;
        }
    }
}
