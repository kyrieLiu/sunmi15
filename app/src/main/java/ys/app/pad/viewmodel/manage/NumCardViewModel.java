package ys.app.pad.viewmodel.manage;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.greendao.NumCardListInfoDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.NumCardActivity;
import ys.app.pad.adapter.manage.NumCardListAdapter;
import ys.app.pad.databinding.ActivityNumCardListBinding;
import ys.app.pad.db.GreenDaoUtils;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.wrapperRecylerView.DividerItemDecoration;

/**
 * Created by aaa on 2017/3/16.
 */

public class NumCardViewModel extends BaseActivityViewModel {

    private NumCardActivity mActivity;
    private ActivityNumCardListBinding mBinding;
    private ApiClient<BaseListResult<NumCardListInfo>> mClient;
    private NumCardListAdapter mAdapter;
    private RxManager mRxManager;
    private NumCardListInfoDao dao ;


    public NumCardViewModel(NumCardActivity activity, ActivityNumCardListBinding binding) {
        this.mActivity = activity;
        this.mBinding = binding;
        this.mClient = new ApiClient<BaseListResult<NumCardListInfo>>();
        registerBus();
        dao = GreenDaoUtils.getSingleTon().getmDaoSession().getNumCardListInfoDao();
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

        mAdapter = new NumCardListAdapter(mActivity,mBinding.recyclerView);
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
        Map<String, String> params = new HashMap<>();
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("limit","1000");
        isNoneDataVisible.set(false);
        isNetWorkErrorVisible.set(false);
        mActivity.showRDialog();
        mClient.reqApi("selectVipCardNumber", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<NumCardListInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<NumCardListInfo> vipCardInfoBaseListResult) {
                        if (vipCardInfoBaseListResult.isSuccess()) {
                            List<NumCardListInfo> list = vipCardInfoBaseListResult.getData();
                            dao.deleteAll();
                            dao.insertInTx(list);
                        }
                    }
                })
                .updateUI(new Callback<BaseListResult<NumCardListInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<NumCardListInfo> info) {
                        mActivity.hideRDialog();
                        if (info.isSuccess()) {
                            List<NumCardListInfo> list = info.getData();
                            if(list.size()>0){
                                mAdapter.setList(list);
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
