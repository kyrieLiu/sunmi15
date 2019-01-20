package ys.app.pad.viewmodel.vip;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableLong;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.greendao.NumCardListInfoDao;
import com.greendao.VipCardInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.vip.AddVipActivity;
import ys.app.pad.activity.vip.PayActivity;
import ys.app.pad.adapter.SelectNumCardAdapter;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.adapter.SelectVipCardAdapter;
import ys.app.pad.databinding.ActivityAddVipBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.ChargeInfo;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.model.VipInfo;
import ys.app.pad.nfc.NFCActivity;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.CardSelectDialog;
import ys.app.pad.widget.dialog.RechargeDialog;
import ys.app.pad.widget.dialog.SelectDialog;

/**
 * Created by aaa on 2017/3/1.
 */

public class AddVipViewModel extends BaseActivityViewModel {
    private VipInfo mInfo;
    private ActivityAddVipBinding mBinding;
    private AddVipActivity mActivity;
    private ApiClient<BaseResult> mClient;
    private ApiClient<BaseListResult<ChargeInfo>> chargClient;
    private ObjectAnimator closeAnim;
    private ObjectAnimator openAnim;

    public ObservableField<String> obVipName = new ObservableField<>();
    public ObservableField<String> obVipTel = new ObservableField<>();
    public ObservableField<String> obVipCardNo = new ObservableField<>();
    public ObservableLong obVipType = new ObservableLong();
    public ObservableField<String> obVipTypeName = new ObservableField<>();
    public ObservableField<String> employeeName = new ObservableField<>();
    private long mId;
    private SelectDialog mSelectDialog;
    private RxManager mRxManager;
    public ObservableBoolean obButtonEnable = new ObservableBoolean(false);
    private RechargeDialog rechargeDialog;
    private double mRealChargeMoney;
    public int flag=-1;
    private SelectInfo employeeInfo;
    private VipCardInfo vipCardInfo;
    private CardSelectDialog cardSelectDialog;
    private int isBirthdayDisCount;

    public AddVipViewModel(AddVipActivity activity, ActivityAddVipBinding binding,int flag) {
        this.mActivity = activity;
        this.mClient = new ApiClient<>();
        chargClient=new ApiClient<>();
        this.mBinding = binding;
        this.flag=flag;
        initPageView();
    }

    public TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean b1 = !StringUtils.isEmptyOrWhitespace(obVipName.get());
            boolean b2 = StringUtils.isPhone(obVipTel.get());
            boolean b3 = !StringUtils.isEmptyOrWhitespace(obVipCardNo.get());
            boolean b4 = !StringUtils.isEmptyOrWhitespace(obVipTypeName.get());
            obButtonEnable.set(b1 && b2 && b3 && b4);
        }
    };

    public void setData(VipInfo info) {
        this.mInfo = info;
        if (mInfo != null) {
            obVipName.set(mInfo.getName());
            obVipTel.set(mInfo.getPhone());
            obVipCardNo.set(mInfo.getCardNo());
            obVipType.set(mInfo.getType());
            obVipTypeName.set(mInfo.getTypeName());
            mId = mInfo.getId();
            if (flag==1){
                mBinding.cardNoEt.setEnabled(false);
                mBinding.ivDialogSearchCard.setEnabled(false);
                mBinding.vipTypeTv.setEnabled(false);
                mBinding.selectIv.setEnabled(false);
            }else{
                if (mInfo.getIsPetBirthdayDiscount()==1){
                    mBinding.rbAddVipSelectBirthday.setChecked(true);
                    mBinding.llAddVipBirthday.setVisibility(View.VISIBLE);
                    mBinding.addVipBirthdayName.setText(mInfo.getPetBirthdayDiscountName());
                }else{
                    mBinding.rbAddVipNotSelectBirthday.setChecked(true);
                }
            }
        }
    }

    public void getRandomCarNo() {
        obVipCardNo.set(AppUtil.getOrderNum());
    }

    private void initPageView(){
        if (flag==0){
            mBinding.llAddVipSelect.setVisibility(View.VISIBLE);
            mBinding.rgAddVipBirthday.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId== R.id.rb_add_vip_select_birthday){//选择生日折扣
                        mBinding.llAddVipBirthday.setVisibility(View.VISIBLE);
                        isBirthdayDisCount=1;

                    }else{//不选择生日折扣
                        mBinding.llAddVipBirthday.setVisibility(View.GONE);
                        isBirthdayDisCount=0;
                    }
                }
            });
        }else{
            mBinding.llAddVipEmployee.setVisibility(View.VISIBLE);
        }
        if ("T1mini".equals(Build.MODEL)){
            mBinding.ivDialogSearchCard.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 在xml布局中绑定了  格式必须是  public   方法名字(View V)
     *
     * @param v
     */
    public void clickButton(View v) {

        if (null != mInfo) {//修改
            updateVipHttp();
        } else if (flag == 1){
            addVipHttp();
        }else {
            addVipHttp();
        }
    }

    /**
     * 点击是否充值
     */
    private void showRechargeDialog() {
        if (rechargeDialog == null){
            rechargeDialog = new RechargeDialog(mActivity);
            rechargeDialog.setContent("需要充值"+mRealChargeMoney+"元,是否去充值？");
            rechargeDialog.setCancelVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rechargeDialog.dismiss();
                    mActivity.finish();
                }
            });
            rechargeDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chargeHttp();
                    rechargeDialog.dismiss();
                }
            });
        }
        rechargeDialog.show();
    }


    public void clickSelectButton(View v) {//点击选择卡的类型
        if (flag == 1){
            selectNumCardDialog();
        }else {
            selectDialog();
        }
    }

    /**
     * 选择会员卡
     */
    private void selectDialog() {
        if (mSelectDialog == null) {
            VipCardInfoDao vipCardInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getVipCardInfoDao();
            QueryBuilder qb = vipCardInfoDao.queryBuilder();
            qb.where(VipCardInfoDao.Properties.Classification.eq(flag));
            final List<VipCardInfo> vipCardInfos = qb.list();
            final List<VipCardInfo> filterList=new ArrayList<>();
            //过滤掉已经作废的卡
            for (VipCardInfo info:vipCardInfos){
                if (info.getState()==0){
                    filterList.add(info);
                }
            }
            if (filterList.size()==0){
                Toast.makeText(mActivity,"暂无可用的会员卡,请添加会员卡",Toast.LENGTH_SHORT).show();
                return;
            }
            SelectVipCardAdapter adapter = new SelectVipCardAdapter(mActivity, filterList);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    VipCardInfo info = filterList.get(position);
                    obVipType.set(info.getId());
                    obVipTypeName.set(info.getName());
                }
            });
        }
        mSelectDialog.show();

    }

    /**
     * 选择次卡
     */
    private void selectNumCardDialog() {
        if (mSelectDialog == null) {
            NumCardListInfoDao numCardListInfoDao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardListInfoDao();
            final List<NumCardListInfo> numCardListInfos = numCardListInfoDao.loadAll();
            final List<NumCardListInfo> filterList = new ArrayList<>();
            //过滤掉已经作废的卡
            for (NumCardListInfo info:numCardListInfos){
                if (info.getState()==0){
                    filterList.add(info);
                }
            }
            if (filterList.size()==0){
                Toast.makeText(mActivity,"暂无可用的会员卡,请添加会员卡",Toast.LENGTH_SHORT).show();
                return;
            }
            SelectNumCardAdapter adapter = new SelectNumCardAdapter(mActivity, filterList);
            mSelectDialog = new SelectDialog(mActivity, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    NumCardListInfo info = filterList.get(position);
                    obVipType.set(info.getId());
                    obVipTypeName.set(info.getName());
                    mRealChargeMoney=filterList.get(position).getRealAmt();
                }
            });
        }
        mSelectDialog.show();
    }
    /**如果是次卡就充值
     */
    private void chargeHttp() {
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("rechargeAmt", mRealChargeMoney+"");
        params.put("userId", employeeInfo.getType() + "");
        params.put("userName", employeeInfo.getName());
        if (!StringUtils.isEmptyOrWhitespace(obVipCardNo.get())) {
            params.put("vipUserCardNo", obVipCardNo.get());
        }
        params.put("phone", obVipTel.get());
        params.put("isClassification", flag+"");
        mActivity.showRDialog();
        chargClient.reqApi("insertRecharge", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<ChargeInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<ChargeInfo> result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            final List<ChargeInfo> data = result.getData();
                            if (null != data) {
                                if (data.size() == 1) {//直接去支付页面
                                    ChargeInfo chargeInfo = data.get(0);
                                    chargeInfo.setChargeMoney(mRealChargeMoney+"");
                                    chargeInfo.setPresentAmt("0");
                                    Intent intent = new Intent(mActivity, PayActivity.class);
                                    intent.putExtra(Constants.intent_info, chargeInfo);
                                    mActivity.startActivity(intent);
                                    mActivity.finish();
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

    private void addVipHttp() {
        if (flag==1){
            if (employeeInfo==null){
                showToast(mActivity,"请选择员工");
                return;
            }
        }
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId()+"");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("name", obVipName.get());
        params.put("phone", obVipTel.get());
        params.put("cardNo", obVipCardNo.get());
        params.put("type", obVipType.get() + "");
        params.put("typeName", obVipTypeName.get());
        if (isBirthdayDisCount==0){
            params.put("isPetBirthdayDiscount","0");
        }else{
            params.put("isPetBirthdayDiscount","1");
            if (vipCardInfo==null){
                showToast(mActivity,"请选择生日折扣卡");
                return;
            }
            params.put("petBirthdayDiscountType",vipCardInfo.getId()+"");
            params.put("petBirthdayDiscountName",vipCardInfo.getName());
        }
        mActivity.showRDialog();
        mClient.reqApi("insertVip", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {

                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_updateVip_success);
                            if (flag==1){//如果是次卡,提示是否充值
                                showRechargeDialog();
                                mBinding.llAddVipEmployee.setVisibility(View.VISIBLE);
                            }else{
                                mActivity.finish();
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


    private void updateVipHttp() {
        mActivity.showRDialog();
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId()+"");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("id", mId + "");
        params.put("name", obVipName.get());
        params.put("phone", obVipTel.get());
        params.put("cardNo", obVipCardNo.get());
        params.put("type", obVipType.get() + "");
        params.put("typeName", obVipTypeName.get());
        params.put("cardState","0");
        if (isBirthdayDisCount==0){
            params.put("isPetBirthdayDiscount","0");
        }else{
            params.put("isPetBirthdayDiscount","1");
            if (StringUtils.isEmpty(mBinding.addVipBirthdayName.getText().toString())){
                showToast(mActivity,"请选择生日折扣卡");
                return;
            }
            if (vipCardInfo!=null){
                params.put("petBirthdayDiscountType",vipCardInfo.getId()+"");
                params.put("petBirthdayDiscountName",vipCardInfo.getName());
            }else if (mInfo!=null){
                params.put("petBirthdayDiscountType",mInfo.getPetBirthdayDiscountType()+"");
                params.put("petBirthdayDiscountName",mInfo.getPetBirthdayDiscountName());
            }


        }

        mClient.reqApi("updateVip", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_updateVip_success);
                            mActivity.finish();
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
    private SelectDialog employeeSelect;
    public void selectEmployee(View view){
        List<EmployeeInfo> mEmployeeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().loadAll();
        if (employeeSelect == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            for (EmployeeInfo info:mEmployeeInfos){
                genderInfos.add(new SelectInfo(info.getName(), info.getId()+""));
            }
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
            employeeSelect = new SelectDialog(mActivity, adapter);
            employeeSelect.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    employeeInfo = genderInfos.get(position);
                    employeeName.set(employeeInfo.getName());
                }
            });
        }
        employeeSelect.show();
    }

    /**
     * 关闭卡类型动画
     */
    private void closeAnim() {
//        mBinding.selectIv.clearAnimation();
        if (closeAnim == null) {
            closeAnim = ObjectAnimator.ofFloat(mBinding.selectIv, "rotationX", 180, 0);
            closeAnim.setDuration(100);
        }
        closeAnim.start();
    }


    /**
     * 打开卡类型动画
     */
    private void openAnim() {
//        mBinding.selectIv.clearAnimation();
        if (closeAnim == null) {
            openAnim = ObjectAnimator.ofFloat(mBinding.selectIv, "rotationX", 0, 180);
            openAnim.setDuration(100);
        }
        openAnim.start();
    }
    public void searchCardNo(){
        Intent intent=new Intent(mActivity, NFCActivity.class);
        intent.putExtra(Constants.intent_flag,"xinzenghuiyuan");
        mActivity.startActivityForResult(intent,10);
    }
    public void actvityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==10){
            if (resultCode==10){
                String cardNo=data.getStringExtra("cardNo");
                obVipCardNo.set(cardNo);
            }
        }
    }
    public void showCardList(){
        if (cardSelectDialog==null){
            cardSelectDialog = new CardSelectDialog(mActivity,mActivity,3);
            cardSelectDialog.setCardSelectListener(new CardSelectDialog.CardSelectListenner() {
                @Override
                public void onCardSelect(VipCardInfo vipCardInfo) {
                    AddVipViewModel.this.vipCardInfo=vipCardInfo;
                    mBinding.addVipBirthdayName.setText(vipCardInfo.getName());
                }
            });
        }
        cardSelectDialog.show();
    }

    public void destroy() {
        if (mClient !=  null){
            mClient.reset();
            mClient = null;
        }
        if (openAnim != null){
            openAnim.cancel();
            openAnim = null;
        }
        mSelectDialog = null;
        if (closeAnim != null){
            closeAnim.cancel();
            closeAnim = null;
        }
        if (mRxManager != null){
            mRxManager.clear();
            mRxManager = null;
        }
        mActivity = null;
    }

}
