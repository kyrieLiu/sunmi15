package ys.app.pad.adapter.vip;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemVipPetChildBinding;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by admin on 2017/7/5.
 */

public class VipPetChildAdapter extends RecyclerView.Adapter<VipPetChildAdapter.MyViewHolder> {
    private final Context mContext;

    public List<AnimalInfo> getList() {
        return mList;
    }

    private List<AnimalInfo> mList;

    private OnItemClickListener listener;


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public VipPetChildAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;


    }

    @Override
    public VipPetChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVipPetChildBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_vip_pet_child, parent, false);
        return new VipPetChildAdapter.MyViewHolder(binding,  listener);
    }

    @Override
    public void onBindViewHolder(VipPetChildAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if (registerI != mList.size()) {
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public void setList(List<AnimalInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends IViewHolder {

        private ItemVipPetChildBinding mBinding;
        private final OnItemClickListener mListenner;
        public MyViewHolder(@NonNull ItemVipPetChildBinding binding, OnItemClickListener listener) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mListenner = listener;
        }

        public void bind(final AnimalInfo itemModle, final int position, final Context mContext) {
            mBinding.tvItemVipPetName.setText("宠物名称: "+itemModle.getName()+" ("+itemModle.getVarietiesName()+")");
            if (itemModle.getUndoneBrithday()>0){
                mBinding.tvItemVipPetUndoneBrithday.setText("宠物生日提醒,宠物今日生日");
                mBinding.tvItemVipPetUndoneBrithday.setVisibility(View.VISIBLE);
            }else{
                mBinding.tvItemVipPetUndoneBrithday.setVisibility(View.GONE);
            }
            if (itemModle.getUndoneInsect()>0){
                mBinding.tvItemVipPetUndoneInsect.setText("宠物内驱提醒");
                mBinding.tvItemVipPetUndoneInsect.setVisibility(View.VISIBLE);
            }else{
                mBinding.tvItemVipPetUndoneInsect.setVisibility(View.GONE);
            }
            if (itemModle.getUndoneInsectOut()>0){
                mBinding.tvItemVipPetUndoneInsectOut.setText("宠物外驱提醒");
                mBinding.tvItemVipPetUndoneInsectOut.setVisibility(View.VISIBLE);
            }else{
                mBinding.tvItemVipPetUndoneInsectOut.setVisibility(View.GONE);
            }
            if (itemModle.getUndoneVaccine()>0){
                mBinding.tvItemVipPetUndoneVaccine.setText("宠物疫苗提醒");
                mBinding.tvItemVipPetUndoneVaccine.setVisibility(View.VISIBLE);
            }else{
                mBinding.tvItemVipPetUndoneVaccine.setVisibility(View.GONE);
            }


        }

    }
}

