package ys.app.pad.adapter;

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
import ys.app.pad.databinding.ItemConfirmCheckOutBinding;
import ys.app.pad.model.VipInfo;
import ys.app.pad.viewmodel.ConfirmCheckOutItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/6/9.
 */

public class ConfirmCheckOutAdapter extends RecyclerView.Adapter<ConfirmCheckOutAdapter.MyViewHolder> {

    private final Context mContext;

    public List<VipInfo> getList() {
        return mList;
    }

    private List<VipInfo> mList = new ArrayList<>();
    private static OnItemClickListener listener;
    private boolean isGuadan;

    public ConfirmCheckOutAdapter(Context context,boolean isGuadan) {
        this.mContext = context;
        this.isGuadan=isGuadan;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ConfirmCheckOutAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemConfirmCheckOutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_confirm_check_out, parent, false);
        return new ConfirmCheckOutAdapter.MyViewHolder(binding,isGuadan);
    }

    @Override
    public void onBindViewHolder(ConfirmCheckOutAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<VipInfo> list) {
        if (list != null && !list.isEmpty()) {
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        this.mList.clear();
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemConfirmCheckOutBinding mBinding;
        private boolean isGuadan;

        public MyViewHolder(@NonNull ItemConfirmCheckOutBinding binding,boolean isGuadan) {
            super(binding.rootView);
            this.mBinding = binding;
            this.isGuadan=isGuadan;
        }

        public void bind(VipInfo itemModle, final int position, Context mContext) {
            ConfirmCheckOutItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ConfirmCheckOutItemViewModel(itemModle, mContext,isGuadan);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }

            if (mBinding != null) {
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(root, position);
                        }
                    }
                });
            }
        }
    }
}
