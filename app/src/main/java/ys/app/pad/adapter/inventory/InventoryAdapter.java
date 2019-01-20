package ys.app.pad.adapter.inventory;

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
import ys.app.pad.databinding.ItemInventoryBinding;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.viewmodel.inventory.InventoryItemViewModel;

/**
 * Created by aaa on 2017/3/3.
 */

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.MyViewHolder> {

    private final Context mContext;
    private List<GoodTypeInfo> mList;
    private static OnItemClickListener listener;

    public InventoryAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public InventoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemInventoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_inventory, parent, false);
        return new InventoryAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InventoryAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<GoodTypeInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private ItemInventoryBinding mBinding;

        public MyViewHolder(@NonNull ItemInventoryBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(GoodTypeInfo itemModle, final int position, Context mContext) {
            InventoryItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new InventoryItemViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
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

