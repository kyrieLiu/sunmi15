package ys.app.pad.nfc;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.vip.VipDetailActivity;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.VipInfo;
import ys.app.pad.widget.dialog.RequestDialog;

/**
 * Created by liuyin on 2017/9/29.
 */

public class VipCardModel {
    private Fragment fragment;
    private String intentFlag;
    private  RxManager rxManager;
    private RequestDialog mRDialog;
    private int vipFlag;//0代表会员卡,1代表次卡

    public VipCardModel(Fragment fragment,String intentFlag,int vipFlag){
        this.fragment=fragment;
        this.intentFlag=intentFlag;
        this.vipFlag=vipFlag;
    }

    /**
     * 查找会员
     */
    private  ApiClient<BaseListResult<VipInfo>> mVipApiClient;
    public  void getVip(final String cardNo){

        if (mVipApiClient==null){
            mVipApiClient = new ApiClient<>();
        }
        Map<String, String> params = new HashMap<>();
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("cardNo", cardNo);
        params.put("classification",vipFlag+"");
        if (vipFlag==1)params.put("branchId",SpUtil.getBranchId()+"");
        showRDialog();
        mVipApiClient.reqApi("selectVip", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<VipInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipInfo> vipInfoBaseListResult) {
                        hideRDialog();
                        List<VipInfo> data = vipInfoBaseListResult.getData();
                        if (data != null && !data.isEmpty()) {
                            for (VipInfo info : data) {
                                if (cardNo.equals(info.getCardNo())) {
                                    turnNext(info);
                                    break;
                                }
                            }
                        }else{
                            Toast.makeText(fragment.getActivity(),"暂无相关会员",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        hideRDialog();
                        Toast.makeText(fragment.getActivity(),"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void turnNext(VipInfo info){
        if ("huiyuan".equals(intentFlag)){
            Intent intent = new Intent(fragment.getActivity(),VipDetailActivity.class);
            intent.putExtra(Constants.intent_info,info);
            intent.putExtra(Constants.intent_type,vipFlag);
            fragment.getActivity().startActivity(intent);
            fragment.getActivity().finish();
        }else if ("shouyin".equals(intentFlag)){
            if (rxManager==null){
                rxManager = new RxManager();
            }
            rxManager.post(Constants.confirm_type_info,info);
            fragment.getActivity().finish();
        }

    }

    public void showRDialog() {
        if(mRDialog == null){
            mRDialog = new RequestDialog(fragment.getActivity());
        }
        mRDialog.show();
    }
    public void hideRDialog() {
        if(mRDialog != null){
            mRDialog.hide();
        }
    }
    public void clear(){
        if(mRDialog!=null){
            mRDialog.dismiss();
        }
    }
}
