package ys.app.pad.viewmodel.appointment;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.appointment.AppointmentActivity;
import ys.app.pad.activity.appointment.AppointmentMainActivity;
import ys.app.pad.adapter.appointment.AppointmentMainAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ActivityAppointmentMainBinding;
import ys.app.pad.event.RxManager;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.AppointmentMainBean;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.utils.NetWorkUtil;

/**
 * Created by liuyin on 2017/10/13.
 */

public class AppointmentMainViewModel extends BaseActivityViewModel {
    private ActivityAppointmentMainBinding mBinding;
    private AppointmentMainActivity mActivity;
    private ApiClient<BaseListResult<AppointmentMainBean>> mClient;
    private AppointmentMainAdapter mAdapter;
    private RxManager rxManager;

    public AppointmentMainViewModel(ActivityAppointmentMainBinding mBinding, AppointmentMainActivity mActivity){
        this.mBinding=mBinding;
        this.mActivity=mActivity;
        this.mClient=new ApiClient<>();
        initView();
    }
    private void initView(){

//        Intent startIntent = new Intent(mActivity, InitDataService.class);
//        mActivity.startService(startIntent);

        if (rxManager==null){
            rxManager=new RxManager();
        }
        rxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if(Constants.type_addAppointment_success == integer){
                    loadHttpData();
                }
            }
        });


        mAdapter = new AppointmentMainAdapter(mActivity);
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(mActivity,4));
        mBinding.recyclerView.setAdapter(mAdapter);



        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mActivity,AppointmentActivity.class);
                TextView tv_date= (TextView) view.findViewById(R.id.tv_appointment_day);
                String date=tv_date.getText().toString();
                intent.putExtra(Constants.intent_flag,date);
                mActivity.startActivity(intent);
            }
        });


        loadHttpData();
    }
    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(mActivity)) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
            mActivity.showToast(Constants.network_error_warn);
            isNetWorkErrorVisible.set(true);
        }
    }

    private void getDataList(){
        Map<String, String> params = new HashMap<>();
        params.put("headOfficeId", SpUtil.getHeadOfficeId()+"");
        params.put("branchId",SpUtil.getBranchId()+"");
        isLoadingVisible.set(true);
        isNetWorkErrorVisible.set(false);
        isNetWorkErrorVisible.set(false);
        mClient.reqApi("selectBespeakByDay", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AppointmentMainBean>>() {
                    @Override
                    public void onSuccess(BaseListResult<AppointmentMainBean> result) {
                        isLoadingVisible.set(false);
                        if (result.isSuccess()) {
                            List<AppointmentMainBean> data = result.getData();
                            mAdapter.setList(data);
                        } else {
                            mActivity.showToast(result.getErrorMessage());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        isLoadingVisible.set(false);
                        isNetWorkErrorVisible.set(true);
                    }
                });

    }

    public void clear(){
        if (rxManager!=null){
            rxManager.clear();
            rxManager=null;
        }
    }

}
