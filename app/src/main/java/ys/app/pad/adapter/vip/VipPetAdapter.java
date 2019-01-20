package ys.app.pad.adapter.vip;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.VipPetListBinding;
import ys.app.pad.itemmodel.VipPetItemViewModel;
import ys.app.pad.model.VipInfo;

/**
 * Created by aaa on 2017/3/16.
 */

public class VipPetAdapter extends RecyclerView.Adapter<VipPetAdapter.MyViewHolder> {

    private final BaseActivity mBaseActivity;
    private List<VipInfo> mList;

    public static void setListener(OnItemClickListener listener) {
        VipPetAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public VipPetAdapter(BaseActivity activity)
    {
        mList = new ArrayList<>();
        this.mBaseActivity = activity;
    }

    @Override
    public VipPetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VipPetListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mBaseActivity), R.layout.item_vip_pet_list, parent, false);
        return new VipPetAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(VipPetAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mBaseActivity);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<VipInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private VipPetListBinding mBinding;

        public MyViewHolder(@NonNull VipPetListBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(VipInfo itemModle, final int position, BaseActivity mBaseActivity) {
            VipPetItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new VipPetItemViewModel(position,itemModle, mBaseActivity);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
                mBinding.setItemViewModel(itemViewModel);
            }


            if(mBinding!=null){
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            listener.onItemClick(root, position);
                        }
                    }
                });
            }
        }
    }
}

