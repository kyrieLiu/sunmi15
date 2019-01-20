package ys.app.pad.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import ys.app.pad.R;
import ys.app.pad.adapter.SelectAdapter;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.DiscountInfo;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.widget.FullyLinearLayoutManager;

/**
 * Created by aaa on 2017/3/20.
 */

public class SelectDialog extends Dialog{

    private Context activity;

    private RecyclerView mRecyclerView;
    private SelectAdapter mAdapter;
    private SelectListenner listenner;



    public SelectDialog(Context activity, SelectAdapter adapter) {
        super(activity, R.style.ThemeCustomDialog);
        super.setContentView(R.layout.select_dialog);
        this.activity = activity;
        this.mAdapter = adapter;
        init();
    }

    private void init() {
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new FullyLinearLayoutManager(activity));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int selectPosition = mAdapter.getPosition();
                if(-1 != selectPosition){
                    if(listenner!=null){
                        listenner.onSelect(selectPosition);
                        dismiss();
                    }
                }else {
                    Toast.makeText(activity,"请选择后再确定",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //setCloseShow();
    }

    public void setData(List<GoodTypeInfo> list){
        mAdapter.setData(list);
    }

    public void setDiscountData(List<DiscountInfo> list){
        mAdapter.setData(list);
    }

    public void setServiceData(List<ServiceTypeInfo> list){
        mAdapter.setData(list);
    }

    public void setListenner(SelectListenner listenner) {
        this.listenner = listenner;
    }

    public void setModifyTransactionData(List<EmployeeInfo> list) {
        mAdapter.setData(list);
    }


    public interface SelectListenner{
        void onSelect(int position);
    }

}
