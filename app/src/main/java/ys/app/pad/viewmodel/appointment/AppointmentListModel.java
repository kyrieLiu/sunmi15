package ys.app.pad.viewmodel.appointment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.functions.Action1;
import ys.app.pad.BaseFragmentViewModel;
import ys.app.pad.Constants;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.vip.VipDetailActivity;
import ys.app.pad.adapter.appointment.AppointmentAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.event.RxManager;
import ys.app.pad.fragment.appointment.AppointmentListFragment;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.itemmodel.AppointmentBean;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.widget.dialog.RequestDialog;
import ys.app.pad.widget.wrapperRecylerView.IRecyclerView;
import ys.app.pad.widget.wrapperRecylerView.LoadMoreFooterView;
import ys.app.pad.widget.wrapperRecylerView.OnLoadMoreListener;
import ys.app.pad.widget.wrapperRecylerView.OnRefreshListener;


/**
 * Created by liuyin on 2017/9/11.
 */

public class AppointmentListModel extends BaseFragmentViewModel {
    private AppointmentListFragment fragment;
    private int intentFrom;
    private IRecyclerView recyclerView;
    private AppointmentAdapter adapter;
    private ApiClient<BaseListResult<AppointmentBean>> mClient;
    private RequestDialog mRDialog;
    private RxManager mRxManager;
    private LoadMoreFooterView mLoadMoreFooterView;
    private String date;
    public AppointmentListModel(AppointmentListFragment fragment, int intentFrom, String date, IRecyclerView recyclerView){
        this.fragment=fragment;
        this.intentFrom=intentFrom;
        this.date=date;
        this.recyclerView=recyclerView;
        this.mClient=new ApiClient<>();
    }
    public void initAppointment(){

        if(mRxManager == null){
            mRxManager = new RxManager();
        }
        mRxManager.on(Constants.bus_type_http_result, new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                if(Constants.type_addAppointment_success == integer){
                    startHttp = 0;
                    loadHttpData();
                }
            }
        });

        adapter=new AppointmentAdapter(fragment.getActivity());
        if (intentFrom==1){
            adapter.setAppointmentAready(true);
        } else if (intentFrom==0){
            adapter.setAppointmentWaiting(true);
        }else if (intentFrom==4){
            adapter.setAppointmentFailer(true);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getActivity()));
        recyclerView.setIAdapter(adapter);


        mLoadMoreFooterView = (LoadMoreFooterView) recyclerView.getLoadMoreFooterView();

        recyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Normal);
                startHttp = 0;
                loadHttpData();
            }
        });


        recyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mLoadMoreFooterView.canLoadMore() && adapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
        mLoadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            @Override
            public void onRetry(LoadMoreFooterView view) {
                if (mLoadMoreFooterView.canLoadMore() && adapter.getItemCount() > 0) {
                    mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.Loading);
                    loadHttpData();
                }
            }
        });
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(fragment.getActivity(),VipDetailActivity.class);
                AppointmentBean bean= adapter.getList().get(position);
                Log.i("info","获取的预约条目"+bean.getUserName());
            }
        });

        loadHttpData();



    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(fragment.getActivity())) {//有网请求数据
            if (firstCome) {
                isLoadingVisible.set(true);
            }
            getDataList();
        } else {//无网显示断网连接
            fragment.showToast(Constants.network_error_warn);
            afterRefreshAndLoadMoreFailed(recyclerView, mLoadMoreFooterView);
        }
    }
    private void getDataList(){
        Map<String, String> params = new HashMap<>();
        params.put("headOfficeId", SpUtil.getHeadOfficeId() + "");
        params.put("branchId",SpUtil.getBranchId()+"");
        params.put("state",intentFrom+"");

        String month=date.substring(0,2);
        int selectMonth=Integer.parseInt(month);
        Calendar mCalendar = Calendar.getInstance();
        int currentMonth = mCalendar.get(Calendar.MONTH) + 1;
        int currentYear = mCalendar.get(Calendar.YEAR);
        if (currentMonth>selectMonth){
            currentYear++;
        }
        String day=currentYear+"年"+date;

        params.put("bespeakDay",date);
        params.put("start",startHttp+"");

        mClient.reqApi("selectBespeak", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .updateUI(new Callback<BaseListResult<AppointmentBean>>() {
                    @Override
                    public void onSuccess(BaseListResult<AppointmentBean> result) {
                        if (result.isSuccess()) {
                            List<AppointmentBean> data = result.getData();
                            adapter.setList(startHttp, data);
                            if (data == null) {
                                afterRefreshAndLoadMoreFailed(recyclerView, mLoadMoreFooterView);
                            } else {
                                afterRefreshAndLoadMoreSuccess(data, recyclerView, mLoadMoreFooterView);
                            }
                        } else {
                            Toast.makeText(fragment.getActivity(), result.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Toast.makeText(fragment.getActivity(), "网络异常", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    public void clear() {

        if (mRDialog!=null){
            mRDialog.dismiss();
            mRDialog = null;
        }
        if (mRxManager!=null){
            mRxManager.clear();
            mRxManager=null;
        }

    }
}
