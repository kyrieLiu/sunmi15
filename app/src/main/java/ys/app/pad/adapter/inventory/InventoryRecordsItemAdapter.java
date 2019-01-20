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
import ys.app.pad.databinding.InventoryRecordItemBinding;
import ys.app.pad.model.InventoryRecordsInfo;
import ys.app.pad.viewmodel.inventory.InventoryRecordItemViewModel;

/**
 * Created by aaa on 2017/3/17.
 */

public class InventoryRecordsItemAdapter extends RecyclerView.Adapter<InventoryRecordsItemAdapter.MyViewHolder> {

    private final Context mContext;
    private List<InventoryRecordsInfo.InventoryList2Bean> mList;

    public static void setListener(OnItemClickListener listener) {
        InventoryRecordsItemAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public InventoryRecordsItemAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public InventoryRecordsItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InventoryRecordItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_inventory_record, parent, false);
        return new InventoryRecordsItemAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InventoryRecordsItemAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<InventoryRecordsInfo.InventoryList2Bean > list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private InventoryRecordItemBinding mBinding;

        public MyViewHolder(@NonNull InventoryRecordItemBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(InventoryRecordsInfo.InventoryList2Bean  itemModle, final int position, Context mContext) {
            InventoryRecordItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new InventoryRecordItemViewModel(itemModle, mContext);
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
