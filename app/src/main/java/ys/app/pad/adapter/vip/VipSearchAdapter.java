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
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ItemVipSearchBinding;
import ys.app.pad.model.VipInfo;
import ys.app.pad.viewmodel.vip.VipSearchItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by Administrator on 2017/6/10.
 */

public class VipSearchAdapter extends RecyclerView.Adapter<VipSearchAdapter.MyViewHolder> {
    private final Context mContext;

    public List<VipInfo> getList() {
        return mList;
    }

    private List<VipInfo> mList = new ArrayList<>();
    private  OnItemClickListener listener;
    private OnItemLongClickListener longClicklistener;

    public VipSearchAdapter(Context context) {
        this.mContext = context;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClicklistener) {
        this.longClicklistener = longClicklistener;
    }

    @Override
    public VipSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVipSearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_vip_search, parent, false);
        return new VipSearchAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(VipSearchAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext,listener,longClicklistener);
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
    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemVipSearchBinding mBinding;

        public MyViewHolder(@NonNull ItemVipSearchBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(VipInfo itemModle, final int position, Context mContext, final OnItemClickListener listener, final OnItemLongClickListener longClicklistener) {
            VipSearchItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new VipSearchItemViewModel(itemModle, mContext);
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
                root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (longClicklistener != null) {
                            longClicklistener.onItemLongClick(root, position);
                        }
                        return true;
                    }
                });
                mBinding.tvItemVipMoney.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            listener.onItemClick(root, position);
                        }
                    }
                });
                mBinding.tvItemVipMoney.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (longClicklistener != null) {
                            longClicklistener.onItemLongClick(root, position);
                        }
                        return true;
                    }
                });
            }
        }
    }
}
