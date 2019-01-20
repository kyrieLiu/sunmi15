package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.TakeOutInventoryActivity;
import ys.app.pad.activity.manage.AllotListActivity;
import ys.app.pad.adapter.SelectBackReasonAdapter;
import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.databinding.ActivityTakeOutInventoryBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AllotSelectModel;
import ys.app.pad.model.BackGoodsReasonInfo;
import ys.app.pad.model.BaseResult;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.widget.dialog.SelectDialog;


/**
 * Created by aaa on 2017/3/7.
 */
public class TakeOutInventoryViewModel {

    private  TakeOutInventoryActivity mActivity;
    private ActivityTakeOutInventoryBinding mBinding;
    public GoodInfo mResult;
    public ObservableField<String> obTackOutNum = new ObservableField<>();
    private RxManager mRxManager;
    private ApiClient<BaseResult> mClient;
    private SelectDialog mSelectDialog;
    public ObservableField<String> obBackReason = new ObservableField<>();
    public ObservableBoolean obButtonEnable = new ObservableBoolean();
    public ObservableBoolean allotVisible=new ObservableBoolean(false);
    public ObservableField<String> shopName=new ObservableField<>();
    public ObservableField<String> employeeName=new ObservableField<>();
    private AllotSelectModel allotSelectModel;

    private SelectDialog employeeDialog;
    private String employeeID="";




    public TakeOutInventoryViewModel(TakeOutInventoryActivity activity, ActivityTakeOutInventoryBinding binding, GoodInfo result) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mResult = result;
        this.mRxManager = new RxManager();
        this.mClient = new ApiClient<>();

    }

    public void clickButton(View v){

        Map<String, String> params = new HashMap<>();
        String num = obTackOutNum.get();
        if(StringUtils.isEmptyOrWhitespace(num)){
            Toast.makeText(mActivity,"请输入出库数量",Toast.LENGTH_SHORT).show();
            return;
        }
        int number=mResult.getStockNum()-Integer.parseInt(num);
        if (number<0){
            Toast.makeText(mActivity,"出库数量不能多于"+mResult.getStockNum(),Toast.LENGTH_SHORT).show();
            return;
        }

        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("stockNum",num);
       if ("调拨".equals(obBackReason.get())){
           params.put("num",num+"" );
           allotProduct(params);
       }else{
           params.put("num",number+"" );
           chuku(params);
       }
    }

    //出库
    public void chuku(Map<String, String> params){
        params.put("id",mResult.getId()+"");
        params.put("type", "2");
        if(!StringUtils.isEmptyOrWhitespace(obBackReason.get())){
            params.put("info", obBackReason.get());
        }
        mActivity.showRDialog();
        mClient.reqApi("updateInvertory", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            mRxManager.post(Constants.bus_type_http_result,Constants.type_updateGoods_success);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });
    }
    //调拨
    private void allotProduct(Map<String, String> params){
        if (allotSelectModel==null){
            Toast.makeText(mActivity,"请选择店铺",Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("commodityId",mResult.getId()+"");
        params.put("toBranchId",allotSelectModel.getBranchId()+"");
        params.put("toBranchName",allotSelectModel.getShopName());
        params.put("commodityName",mResult.getName());
        params.put("commodityTypeId",mResult.getType()+"");
        params.put("commodityTypeName",mResult.getTypeName());
        params.put("branchName", SpUtil.getShopName());
        params.put("stockNum",mResult.getStockNum()+"");
        params.put("userId",employeeID);
        if (StringUtils.isEmptyOrWhitespace(employeeName.get())){
            Toast.makeText(mActivity,"请选择调出人",Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("userName",employeeName.get());
        params.put("commodityUnit",mResult.getUnit());
        mActivity.showRDialog();
        mClient.reqApi("insertAllocation", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            Toast.makeText(mActivity,"调拨成功",Toast.LENGTH_SHORT).show();
                            mRxManager.post(Constants.bus_type_http_result,Constants.type_updateGoods_success);
                            mActivity.finish();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                    }
                });
    }

    public void clickSelectButton(View v){
        if(mSelectDialog == null){
            final List<BackGoodsReasonInfo> backReasonList = GreenDaoUtils.getSingleTon().getmDaoSession().getBackGoodsReasonInfoDao().loadAll();
            SelectBackReasonAdapter adapter = new SelectBackReasonAdapter(mActivity,backReasonList);
            mSelectDialog = new SelectDialog(mActivity,adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    String reason=backReasonList.get(position).getReasons();
                    obBackReason.set(reason);
                    if (reason.equals("调拨")){
                        allotVisible.set(true);
                    }else{
                        allotVisible.set(false);
                        shopName.set("");
                    }
                }
            });
        }
        mSelectDialog.show();
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
            boolean b4 = !StringUtils.isEmptyOrWhitespace(obTackOutNum.get());
            if (b4 && s != null) {
                String s1 = s.toString();
                int add = -1;
                if (!TextUtils.isEmpty(s1)) {
                    try{
                        add = Integer.parseInt(s1);
                    }catch (Exception e){

                    }
                    if (add > 0){
                        obButtonEnable.set(true);
                    }else {
                        obButtonEnable.set(false);
                    }
                }
            }
        }
    };
    //选择调出店铺
    public void selectAllotShop(){
        Intent intent=new Intent(mActivity, AllotListActivity.class);
        mActivity.startActivityForResult(intent,10);
    }

    /**
     * 选择调出人
     */
    public void selectOutEmployee(){

        final List<SelectInfo> genderInfos=new ArrayList<>();
        List<EmployeeInfo> mEmployeeInfos = GreenDaoUtils.getSingleTon().getmDaoSession().getEmployeeInfoDao().loadAll();
        for (EmployeeInfo info:mEmployeeInfos){
            SelectInfo selectInfo=new SelectInfo(info.getName(),info.getId()+"");
            genderInfos.add(selectInfo);
        }
        SelectSimpleAdapter adapter = new SelectSimpleAdapter(mActivity, genderInfos);
        if (employeeDialog==null){
            employeeDialog = new SelectDialog(mActivity, adapter);
            employeeDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    employeeName.set(genderInfo.getName());
                    employeeID=genderInfo.getType();
                }
            });
        }
       employeeDialog.show();
    }

    public void activityResult(int requestCode, int resultCode, Intent data){
        if (requestCode==10){
            if (resultCode==10){
                Bundle bundle=data.getExtras();
                allotSelectModel = (AllotSelectModel) bundle.getSerializable("model");
                allotVisible.set(true);
                shopName.set(allotSelectModel.getShopName());
            }
        }
    }

    public void reset() {
        mSelectDialog = null;
        employeeDialog=null;
        if (mClient != null) {
            mClient.reset();
            mClient = null;
        }
        if (mRxManager != null) {
            mRxManager.clear();
            mClient = null;
        }
        mActivity = null;

    }
}
