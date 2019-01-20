package ys.app.pad.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.adapter.manage.VipCardAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.http.ApiClient;
import ys.app.pad.http.ApiRequest;
import ys.app.pad.http.Callback;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.utils.NetWorkUtil;
import ys.app.pad.utils.SpUtil;

/**
 * Created by aaa on 2017/3/20.
 */

public class CardSelectDialog extends Dialog{

    private BaseActivity activity;

    private RecyclerView mRecyclerView;
    private VipCardAdapter mAdapter;
    private ApiClient<BaseListResult<VipCardInfo>> mClient=new ApiClient<>();
    private int classification;
    private ImageView iv_noneData;
    private RelativeLayout rl_progress;


    public CardSelectDialog(Context activity, BaseActivity baseActivity,int classification) {
        super(activity, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.card_select_dialog);
        this.activity = baseActivity;
        this.classification=classification;
        init();
    }

    private void init() {
        TextView tv_close= (TextView) findViewById(R.id.tv_card_select_dialog_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        iv_noneData = (ImageView) findViewById(R.id.noneDataIv);
        rl_progress = (RelativeLayout) findViewById(R.id.rl_dialog_vip_card_progress);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mAdapter = new VipCardAdapter(activity,classification);
        mAdapter.setShowType(1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                dismiss();
                VipCardInfo vipCardInfo=mAdapter.getList().get(position);
                if (selectListenner!=null){
                    selectListenner.onCardSelect(vipCardInfo);
                }
            }
        });
        loadHttpData();

    }

    private void loadHttpData() {
        if (NetWorkUtil.isNetworkAvailable(getContext())) {//有网请求数据
            getDataList();
        } else {//无网显示断网连接
//            isNetWorkErrorVisible.set(true);
//            mActivity.showToast(Constants.network_error_warn);
        }
    }

    public void getDataList() {
        Map<String, String> params = new HashMap<>();
        params.put("branchId", SpUtil.getBranchId()+"");
        params.put("headOfficeId",SpUtil.getHeadOfficeId()+"");
        params.put("limit","1000");
        params.put("isClasssification",classification+"");
        rl_progress.setVisibility(View.VISIBLE);
        mClient.reqApi("queryVipList", params, ApiRequest.RespDataType.RESPONSE_JSON)
                .saveData(new Callback<BaseListResult<VipCardInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipCardInfo> vipCardInfoBaseListResult) {

                    }
                })
                .updateUI(new Callback<BaseListResult<VipCardInfo>>() {
                    @Override
                    public void onSuccess(BaseListResult<VipCardInfo> info) {
                        rl_progress.setVisibility(View.GONE);
                        if (info.isSuccess()) {
                            List<VipCardInfo> list = info.getData();
                            if(list.size()>0){
                                mAdapter.setList(info.getData());
                            }else{
                                iv_noneData.setVisibility(View.VISIBLE);
                            }

                        } else {
                            Toast.makeText(getContext(),info.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        iv_noneData.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        rl_progress.setVisibility(View.GONE);
                        Toast.makeText(getContext(),"网络异常,请检查网络",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private CardSelectListenner selectListenner;
    public void setCardSelectListener(CardSelectListenner selectListenner){
        this.selectListenner=selectListenner;
    }


    public interface CardSelectListenner{
        void onCardSelect(VipCardInfo vipCardInfo);
    }

}
