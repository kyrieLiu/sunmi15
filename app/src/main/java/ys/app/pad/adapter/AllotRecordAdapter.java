package ys.app.pad.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ys.app.pad.R;
import ys.app.pad.utils.SpUtil;
import ys.app.pad.activity.manage.AllotRecordActivity;
import ys.app.pad.databinding.ItemAllotShopRecordBinding;
import ys.app.pad.itemmodel.AllotManageRecordItemModel;
import ys.app.pad.model.AllotRecordInfo;

/**
 * Created by liuyin on 2017/9/16.
 */

public class AllotRecordAdapter extends RecyclerView.Adapter<AllotRecordAdapter.MyViewhHolder>{
    private List<AllotRecordInfo> mList;

    private AllotRecordActivity context;


    public AllotRecordAdapter(List<AllotRecordInfo> mList, AllotRecordActivity context){
        this.mList=mList;
        this.context=context;
    }

    @Override
    public MyViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAllotShopRecordBinding mBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_allot_manage,parent,false);
        return new MyViewhHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(MyViewhHolder holder, int position) {
        holder.bind(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateSort(int registerI) {
        AllotRecordInfo item=mList.get(registerI);
        item.setState(1);
        notifyDataSetChanged();
//        notifyItemRemoved(registerI);
//        if(registerI != list.size()){
//            notifyItemRangeChanged(registerI, list.size() - registerI);
//        }
    }

    public void setList(int startHttp, List<AllotRecordInfo> list) {
        if( 0 == startHttp){
            mList.clear();
        }

        int positionStart = mList.size();
        int itemCount = list.size();
        mList.addAll(list);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }

    }

    class MyViewhHolder extends RecyclerView.ViewHolder{
        ItemAllotShopRecordBinding mBinding;
        public MyViewhHolder(ItemAllotShopRecordBinding mBinding) {
            super( mBinding.rootView);
            this.mBinding=mBinding;
        }
        public void bind(AllotRecordInfo info,int position){
            AllotManageRecordItemModel model;
            if (mBinding.getItemViewModel()==null){
                model=new AllotManageRecordItemModel(info,context,position,AllotRecordAdapter.this);
                mBinding.setItemViewModel(model);
            }else{
                model=mBinding.getItemViewModel();
                model.setModel(info);
            }
            if (info.getBranchId()== SpUtil.getBranchId()){
                mBinding.tvAllotType.setText("调出");
                mBinding.btItemAllotRecord.setVisibility(View.GONE);
                mBinding.tvItemAllotRecordShopName.setText("接收店铺: "+info.getToBranchName());
                if (info.getState()==0){
                    mBinding.tvItemAllotRecordIsCheck.setVisibility(View.GONE);
                }else{
                    mBinding.tvItemAllotRecordIsCheck.setVisibility(View.VISIBLE);
                }
            }else if (info.getToBranchId()==SpUtil.getBranchId()){
                mBinding.tvAllotType.setText("调入");
                mBinding.tvItemAllotRecordShopName.setText("调出店铺: "+info.getBranchName());
                if (info.getState()==0){
                    mBinding.btItemAllotRecord.setVisibility(View.VISIBLE);
                    mBinding.tvItemAllotRecordIsCheck.setVisibility(View.GONE);
                }else{
                    mBinding.btItemAllotRecord.setVisibility(View.GONE);
                    mBinding.tvItemAllotRecordIsCheck.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
