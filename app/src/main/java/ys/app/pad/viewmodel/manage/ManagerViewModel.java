package ys.app.pad.viewmodel.manage;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.activity.LoginActivity;
import ys.app.pad.activity.SystemSettingActivity;
import ys.app.pad.activity.manage.AboutUsActivity;
import ys.app.pad.activity.manage.AllotRecordActivity;
import ys.app.pad.activity.manage.EmployeeListActivity;
import ys.app.pad.activity.manage.ManagerActivity;
import ys.app.pad.activity.manage.ModifyBossPswActivity;
import ys.app.pad.activity.manage.ModifyPswActivity;
import ys.app.pad.activity.manage.NumCardActivity;
import ys.app.pad.activity.manage.VipCardActivity;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.databinding.ActivityManagerBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.http.ApiClient;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.SelectDialog;

/**
 * Created by lyy on 2017/2/28 14:46.
 * email：2898049851@qq.com
 */

public class ManagerViewModel extends BaseActivityViewModel {

    private ActivityManagerBinding mBinding;
    private ApiClient<BaseResult> mClient;
    private ManagerActivity mActivity;
    private CustomDialog mDialog;


    public ManagerViewModel(ManagerActivity activity, ActivityManagerBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<>();
    }

    public void init() {
//        mBinding.employeeNumTv.setText(SpUtil.getEmployeeNum());
//        Intent startIntent = new Intent(mActivity, InitDataService.class);
//        mActivity.startService(startIntent);

    }

    public void clickOkBtn(View v) {
        showDialog();
    }

    public void clickModifyBtn(View v) {
        Intent intent = new Intent(mActivity, ModifyPswActivity.class);
        intent.putExtra(Constants.intent_flag,0);
        mActivity.startActivity(intent);
    }

    public void clickManagerEmployeeBtn(View v) {
        Intent intent = new Intent(mActivity, EmployeeListActivity.class);
        mActivity.startActivity(intent);
    }
    public void clickSystemSetting() {
        Intent intent = new Intent(mActivity, SystemSettingActivity.class);
        mActivity.startActivity(intent);
    }
    private SelectDialog cardDialog;
    public void clickVipCardBtn(View v) {
        if (cardDialog==null){
            final List<SelectInfo> cardList=new ArrayList<>();
            SelectInfo vipInfo=new SelectInfo("会员卡管理","1");
            SelectInfo numInfo=new SelectInfo("次卡管理","2");
            SelectInfo onlyDiscount=new SelectInfo("折扣卡管理","3");
            SelectInfo birthdayDiscount=new SelectInfo("生日折扣管理","4");
            cardList.add(vipInfo);
            cardList.add(numInfo);
            cardList.add(onlyDiscount);
            cardList.add(birthdayDiscount);
            SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, cardList);
           cardDialog = new SelectDialog(mActivity, adapter);
            cardDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    cardDialog.dismiss();
                    SelectInfo genderInfo = cardList.get(position);
                    if ("会员卡管理".equals(genderInfo.getName())) {
                        Intent intent = new Intent(mActivity, VipCardActivity.class);
                        intent.putExtra(Constants.intent_flag,0);
                        mActivity.startActivity(intent);
                    }else if ("折扣卡管理".equals(genderInfo.getName())){
                        Intent intent = new Intent(mActivity, VipCardActivity.class);
                        intent.putExtra(Constants.intent_flag,2);
                        mActivity.startActivity(intent);
                    }else if ("生日折扣管理".equals(genderInfo.getName())){
                        Intent intent = new Intent(mActivity, VipCardActivity.class);
                        intent.putExtra(Constants.intent_flag,3);
                        mActivity.startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(mActivity, NumCardActivity.class);
                        mActivity.startActivity(intent);
                    }
                }
            });
        }

        cardDialog.show();

    }

    /**
     * 调拨管理
     */
    public void clickAllotMange(){
        Intent intent=new Intent(mActivity, AllotRecordActivity.class);
        mActivity.startActivity(intent);
    }

    /**
     * 修改店长密码
     * @param v
     */
    public void clickModifyBossPswBtn(View v) {
        Intent intent = new Intent(mActivity, ModifyBossPswActivity.class);
        mActivity.startActivity(intent);
    }


    public void clickAboutBtn(View v) {
        Intent intent = new Intent(mActivity, AboutUsActivity.class);
        mActivity.startActivity(intent);
    }


    private void showDialog() {
        if (mDialog == null) {
            mDialog = new CustomDialog(mActivity);
            mDialog.setContent("确认退出吗?");
            mDialog.setCancelVisiable();
            mDialog.setOkVisiable(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SpUtil.clearLoginInfo();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getLoginInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getGoodTypeInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getServiceTypeInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getVipCardInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getAnimalTypeClassifyInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().deleteAll();;
                    GreenDaoUtils.getSingleTon().getmDaoSession().getBackGoodsReasonInfoDao().deleteAll();
                    GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardListInfoDao().deleteAll();
                    mDialog.dismiss();
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    mActivity.startActivity(intent);
                    //mActivity.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_from_left);
                    mActivity.finish();
//                    exitHttp();
                }
            });

        }
        mDialog.show();
    }

    public void destroy() {
        mClient.reset();
        mClient = null;
        mActivity = null;
        cardDialog=null;
    }

}
