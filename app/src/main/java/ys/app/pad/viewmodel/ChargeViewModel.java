package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.greendao.NumCardListInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.vip.ChargeActivity;
import ys.app.pad.activity.vip.PayActivity;
import ys.app.pad.adapter.SelectAdapter;
import ys.app.pad.adapter.SelectVipAdapter;
import ys.app.pad.adapter.manage.EmployeeAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ActivityChargeBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.model.VipInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.SelectDialog;
import ys.app.pad.widget.wrapperRecylerView.DividerItemDecoration;

/**
 * Created by aaa on 2017/3/1.
 */

public class ChargeViewModel extends BaseActivityViewModel {
    public ObservableField<String> money = new ObservableField<>();
    public ObservableField<String> presentMoney = new ObservableField<>();
    public ObservableField<String> vipName = new ObservableField<String>();
    public ObservableField<String> vipPhone = new ObservableField<String>();
    public ObservableField<String> vipCardNo = new ObservableField<String>();

    private ActivityChargeBinding mBinding;
    private ChargeActivity mActivity;
    private ApiClient<BaseListResult<ChargeInfo>> mClient;
    public ObservableBoolean obButtonEnable = new ObservableBoolean(false);
    private EmployeeInfo mEmployeeInfo;
    public ObservableField<String> selectEmployee = new ObservableField<>();
    public ObservableLong selectEmployeeId = new ObservableLong();
    private ApiClient<BaseListResult<VipInfo>> mVipApiClient;
    private CustomDialog mCustomDialog;


    public TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
//            boolean b1 = !StringUtils.isEmptyOrWhitespace(obCardNo.get());
//            boolean b2 = StringUtils.isPhone(phone);
            boolean b3 = StringUtils.isValidMoney(money.get());
            boolean b4 = !StringUtils.isEmptyOrWhitespace(selectEmployee.get());
            if (b3 && s != null) {
                String s1 = s.toString();
                if (!TextUtils.isEmpty(s1)) {
                    if (s1.contains(".")) {
                        String[] split = s1.split("\\.");
                        if (split.length > 1 && !TextUtils.isEmpty(split[1])) {
                            if (split[1].length() > 2) {
                                Toast.makeText(mActivity, "只能输入两位小数", Toast.LENGTH_SHORT).show();
                                money.set(split[0] + "." + split[1].substring(0, 2));
                                return;
                            }
                        }
                    }
                }

            }
            obButtonEnable.set(b3 && b4);
        }
    };

    private SelectDialog mDialog;
    private String mRealChargeMoney;
    private List<EmployeeInfo> mEmployeeInfos;
    private View mContentView;
    private PopupWindow mPop;
    private RxManager mRxManager;
    private long mVipId;
    private String presentAmt;
    public ObservableInt vipType=new ObservableInt();//区别会员卡和次卡


    public ChargeViewModel(ChargeActivity activity, ActivityChargeBinding binding) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        this.mBinding = binding;
        mVipApiClient = new ApiClient<>();
        registerBus();
    }

    private void registerBus() {
        if (mRxManager == null) {
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if (Constants.type_charge_finish == integer) {
                    mActivity.finish();
                }
            }
        });
    }
    public void setVipId(long vipId){
        mVipId = vipId;
    }

    public void setVipIntentData(VipInfo vipInfo) {
        mVipId = (int) vipInfo.getId();
        vipPhone.set(vipInfo.getPhone());
        vipCardNo.set(vipInfo.getCardNo());
        vipName.set(vipInfo.getName());
        if (StringUtils.isEmpty(vipInfo.getProductList())){
            vipType.set(0);
        }else{
            vipType.set(1);
            mBinding.moneyEt.setEnabled(false);
            NumCardListInfoDao numCardListInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardListInfoDao();
            QueryBuilder qb = numCardListInfoDao.queryBuilder();
            qb.where(NumCardListInfoDao.Properties.Id.eq(vipInfo.getType()));
            List<NumCardListInfo> list=qb.list();
            if (list.size()>0){
                money.set(list.get(0).getRealAmt()+"");
            }


        }


    }


    public void clickButton(View v) {
        if (!StringUtils.isEmpty(presentMoney.get())) {
            if (mCustomDialog == null) {
                mCustomDialog = new CustomDialog(mActivity);
                mCustomDialog.setContent("确定赠送"+presentMoney.get()+"元吗?");
                mCustomDialog.setCancelVisiable();
            }
            mCustomDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCustomDialog.dismiss();
                    chargeHttp();
                }
            });
            mCustomDialog.show();
        }else{
            chargeHttp();
        }

    }

    private void chargeHttp() {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("rechargeAmt", money.get());
        this.mRealChargeMoney = money.get();
        if (!StringUtils.isEmptyOrWhitespace(vipCardNo.get())) {
            params.put("vipUserCardNo", vipCardNo.get());
        }
        params.put("userId", selectEmployeeId.get() + "");
        params.put("userName", selectEmployee.get());
        presentAmt = presentMoney.get();
        if (StringUtils.isEmpty(presentMoney.get())){
            presentAmt ="0";
        }else{
            presentAmt =presentMoney.get();
        }
        params.put("presentAmt", presentAmt);
        params.put("isClassification", vipType.get()+"");

        mBinding.okBtn.setEnabled(false);
        mClient.reqApi("insertRecharge", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ChargeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ChargeInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            final List<ChargeInfo> data = result.getData();

                            if (null != data) {
                                if (data.size() == 1) {//直接去支付页面
                                    ChargeInfo chargeInfo = data.get(0);
                                    chargeInfo.setChargeMoney(mRealChargeMoney);
                                    chargeInfo.setPresentAmt(presentAmt);
                                    startNewActivity(chargeInfo);

                                } else if (data.size() > 1) {//弹出选择弹窗
                                    if (mDialog == null) {
                                        SelectAdapter adapter = new SelectVipAdapter(mActivity, data);
                                        mDialog = new SelectDialog(mActivity, adapter);
                                        mDialog.setListenner(new SelectDialog.SelectListenner() {
                                            @Override
                                            public void onSelect(int position) {

                                                ChargeInfo chargeInfo = data.get(position);
                                                chargeInfo.setChargeMoney(mRealChargeMoney);
                                                chargeInfo.setPresentAmt(presentAmt);
                                                if (position > 0) {
                                                    chargeInfo.setOrderNo(data.get(0).getOrderNo());
                                                }
                                                startNewActivity(chargeInfo);
                                            }
                                        });
                                    }
                                    mDialog.show();
                                } else {
                                    mActivity.showToast("对不起,没有符合改条件的会员");
                                }
                            }

                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                    }
                });
    }

    private void startNewActivity(ChargeInfo chargeInfo) {

        Intent intent = new Intent(mActivity, PayActivity.class);
        intent.putExtra(Constants.intent_info, chargeInfo);
        mActivity.startActivity(intent);
    }

    public void init() {
        mEmployeeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().loadAll();
        // 一个自定义的布局，作为显示的内容
        mContentView = LayoutInflater.from(mActivity).inflate(R.layout.pop_window, null);
        // 设置按钮的点击事件
        RecyclerView recyclerView = (RecyclerView) mContentView.findViewById(R.id.recyclerView);
        EmployeeAdapter adapter = new EmployeeAdapter(mActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, R.drawable.shape_recyclerview_division_line));
        recyclerView.setAdapter(adapter);
        adapter.setList(mEmployeeInfos);
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ChargeViewModel.this.mEmployeeInfo = mEmployeeInfos.get(position);
                selectEmployee.set(mEmployeeInfo.getName());
                selectEmployeeId.set(mEmployeeInfo.getId());
                if (mPop != null) {
                    mPop.dismiss();
                }
            }
        });

        if (TextUtils.isEmpty(vipPhone.get()) || TextUtils.isEmpty(vipName.get())) {
            Map<String, String> parmars = new HashMap<>();
            parmars.put("vipUserId", mVipId + "");
            parmars.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
            mActivity.showRDialog();
            mVipApiClient.reqApi("selectVip", parmars, ApiRequest.RespDataType.RESPONSE_JSON)
                    .updateUI(new Callback<BaseListResult<VipInfo>>() {
                        @Override
                        public void onSuccess(BaseListResult<VipInfo> vipInfoBaseListResult) {
                            mActivity.hideRDialog();
                            List<VipInfo> data = vipInfoBaseListResult.getData();
                            if (data != null && !data.isEmpty()) {
                                for (VipInfo vipInfo : data) {
                                    if (mVipId == vipInfo.getId()) {
                                        vipName.set(vipInfo.getName());
                                        vipPhone.set(vipInfo.getPhone());
                                        vipCardNo.set(vipInfo.getCardNo());
                                        break;
                                    }
                                }

                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            mActivity.hideRDialog();
                        }
                    });
        }
    }

    public void selectEmployeeClick() {
        mPop = new PopupWindow(mContentView, mBinding.selectEmployeeLl.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPop.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        Drawable drawable = mActivity.getResources().getDrawable(R.drawable.shape_pop_bg);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mPop.setBackgroundDrawable(drawable);
        mPop.showAsDropDown(mBinding.selectEmployeeLl);
    }
    public void clear(){
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
        mCustomDialog=null;
    }
}
