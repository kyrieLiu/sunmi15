package ys.app.pad.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.AddAnimalActivity;
import ys.app.pad.activity.vip.VipDetailActivity;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.model.BaseResult;
import ys.app.pad.widget.dialog.CustomDialog;
import ys.app.pad.widget.dialog.DeleteDialog;

/**
 * Created by aaa on 2017/3/17.
 */

public class AnimalItemViewModel extends BaseObservable {

    private AnimalInfo model;
    private VipDetailActivity mActivity;
    public ObservableInt selectIsYimiaoImg = new ObservableInt(R.mipmap.choose_normal);
    public ObservableInt selectIsQuchongImg = new ObservableInt(R.mipmap.choose_normal);
    public ObservableInt selectAnimalImg = new ObservableInt(R.mipmap.choose_normal);

    private DeleteDialog mConfirmDeleteDialog;
    private CustomDialog mDeleteDialog;
    private ApiClient<BaseResult> mClient;
    private RxManager mRxManager;


    public AnimalItemViewModel(AnimalInfo model, VipDetailActivity mActivity)
    {
        this.mActivity = mActivity;
        if (mClient==null){
            mClient=new ApiClient<>();
        }
        setModel(model);
    }



    @Bindable
    public AnimalInfo getModel() {
        return model;
    }

    public void setModel(AnimalInfo model) {
        this.model = model;
        selectIsYimiaoImg.set(model.getIsVaccine()==1?R.mipmap.select:R.mipmap.choose_normal);
        selectIsQuchongImg.set(model.getIsInsect()==1?R.mipmap.select:R.mipmap.choose_normal);
        int animalImg = R.mipmap.other;
        String varietiesName = model.getVarietiesName();
        if(null!=varietiesName){
            if(varietiesName.contains("狗")||varietiesName.contains("犬")){
                animalImg = R.mipmap.dog;
            }else if(varietiesName.contains("猫")){
                animalImg = R.mipmap.cat;
            }
        }else{
            animalImg = R.mipmap.dog;
        }
        selectAnimalImg.set(animalImg);
    }

    public void onClickEdit(View v){
        Intent intent = new Intent(mActivity,AddAnimalActivity.class);
        intent.putExtra(Constants.intent_info,model);
        mActivity.startActivity(intent);
    }

    /**
     * 查看成本价需要店长密码
     */
    public void deletePet(View view){
        if (mDeleteDialog == null) {
            mDeleteDialog = new CustomDialog(mActivity);
            mDeleteDialog.setContent("确定删除该宠物?");
            mDeleteDialog.setCancelVisiable();
        }

        mDeleteDialog.setOkVisiable(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDeleteDialog.dismiss();
                if (mConfirmDeleteDialog == null){
                    mConfirmDeleteDialog = new DeleteDialog(mActivity);
                }
                mConfirmDeleteDialog.setOkVisiable(new DeleteDialog.IDeleteDialogCallback() {
                    @Override
                    public void verificationPwd(boolean b) {
                        if (b){
                            if (mConfirmDeleteDialog != null){
                                mConfirmDeleteDialog.dismiss();
                                confirmDeletePet();
                            }
                        }else {
                            Toast.makeText(mActivity,"密码输入错误请重新输入",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDismiss() {
                        mConfirmDeleteDialog = null;
                    }
                });
                mConfirmDeleteDialog.show();
            }
        });
        mDeleteDialog.show();
    }
    private void confirmDeletePet(){
        Map<String, String> params = new HashMap<>();
        params.put("shopId", SpUtil.getShopId());
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("id",model.getId()+"");
        params.put("state","4");
        mClient.reqApi("updateAnimal", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        mActivity.hideRDialog();
                        if (result.isSuccess()) {
                            if (mRxManager == null) {
                                mRxManager = new RxManager();
                            }
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_updateAnimal_success);
                            mRxManager.post(Constants.bus_type_http_result, Constants.type_updateVip_success);
                            Toast.makeText(mActivity,"删除成功",Toast.LENGTH_SHORT).show();
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mActivity.hideRDialog();
                        super.onError(e);
                        mActivity.showToast(e.toString());
                    }
                });
    }
}
