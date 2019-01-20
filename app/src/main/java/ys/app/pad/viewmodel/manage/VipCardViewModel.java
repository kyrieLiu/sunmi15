package ys.app.pad.viewmodel.manage;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.greendao.VipCardInfoDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.VipCardActivity;
import ys.app.pad.adapter.manage.VipCardAdapter;
import ys.app.pad.databinding.ActivityVipCardBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.wrapperRecylerView.DividerItemDecoration;

/**
 * Created by aaa on 2017/3/16.
 */

public class VipCardViewModel extends BaseActivityViewModel {

    private VipCardActivity mActivity;
    private ActivityVipCardBinding mBinding;
    private ApiClient<BaseListResult<VipCardInfo>> mClient;
    private VipCardAdapter mAdapter;
    private RxManager mRxManager;
    public List<GoodTypeInfo> goodTypeInfos = new ArrayList<>();
    public List<ServiceTypeInfo> serviceTypeInfos = new ArrayList<>();
    private VipCardInfoDao dao ;
    private int classification;


    public VipCardViewModel(VipCardActivity activity, ActivityVipCardBinding binding,int classification) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<BaseListResult<VipCardInfo>>();
        registerBus();
        goodTypeInfos.addAll(GreenDaoUtils.getmDaoSession().getGoodTypeInfoDao().loadAll());
        serviceTypeInfos.addAll(GreenDaoUtils.getmDaoSession().getServiceTypeInfoDao().loadAll());
        dao = GreenDaoUtils.getSingleTon().getmDaoSession().getVipCardInfoDao();
        this.classification=classification;
    }

    private void registerBus() {
        if (mRxManager == null) {
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer registerI) {
                if (Constants.type_addVipCard_success == registerI || Constants.type_updateVipCard_success == registerI) {
                    getDataList();
                }
            }
        });
        mRxManager.on(Constants.bus_type_delete_position, new Action1<Integer>() {
            @Override
            public void call(Integer registerI) {
                mAdapter.updateSort(registerI);//重新排序
            }
        });
    }

    public void init() {

        mAdapter = new VipCardAdapter(mActivity,classification);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mBinding.recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST, R.drawable.shape_recyclerview_division));
        mBinding.recyclerView.setAdapter(mAdapter);


        loadHttpData();
    }
    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
            isNetWorkErrorVisible.set(true);
            mActivity.showToast(Constants.network_error_warn);
        }
    }

    public void getDataList() {
        mActivity.showRDialog();
        isNoneDataVisible.set(false);
        isNetWorkErrorVisible.set(false);
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("limit","1000");
        params.put("isClasssification",classification+"");

        mClient.reqApi("queryVipList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<VipCardInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipCardInfo> vipCardInfoBaseListResult) {
                        if (classification==0){
                            if (vipCardInfoBaseListResult.isSuccess()) {
                                List<VipCardInfo> list = vipCardInfoBaseListResult.getData();
                                dao.deleteAll();
                                dao.insertInTx(list);
                            }
                        }
                    }
                })
                .updateUI(new Callback<BaseListResult<VipCardInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipCardInfo> info) {
                        mActivity.hideRDialog();
                        if (info.isSuccess()) {
                            List<VipCardInfo> list = info.getData();
                            if(list.size()>0){
                                mAdapter.setList(info.getData());
                            }else{
                                isNoneDataVisible.set(true);
                            }

                        } else {
                            isNetWorkErrorVisible.set(true);
                            mActivity.showToast(info.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mActivity.hideRDialog();
                        isNetWorkErrorVisible.set(true);
                    }
                });

    }

    @Override
    public void reloadData(View view) {
        super.reloadData(view);
        loadHttpData();
    }
    public void clear(){
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }
        if (mClient!=null){
            mClient.reset();
            mClient=null;
        }
    }
}
