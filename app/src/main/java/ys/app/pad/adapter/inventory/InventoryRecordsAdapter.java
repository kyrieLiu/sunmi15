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
import ys.app.pad.databinding.ItemInventoryRecordBinding;
import ys.app.pad.model.InventoryRecordsInfo;
import ys.app.pad.viewmodel.ItemInventoryRecordItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by admin on 2017/7/12.
 */

public class InventoryRecordsAdapter extends RecyclerView.Adapter<InventoryRecordsAdapter.MyViewHolder> {

    private final Context mContext;

    public List<InventoryRecordsInfo> getList() {
        return mList;
    }

    private List<InventoryRecordsInfo> mList;

    private static OnItemClickListener listener;

    public InventoryRecordsAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public InventoryRecordsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemInventoryRecordBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_inventory_records, parent, false);
        return new InventoryRecordsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InventoryRecordsAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }

    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(int startHttp, List<InventoryRecordsInfo> list) {
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



    public static class MyViewHolder extends IViewHolder {

        private ItemInventoryRecordBinding mBinding;

        public MyViewHolder(@NonNull ItemInventoryRecordBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(InventoryRecordsInfo itemModle, final int position, Context mContext) {
            ItemInventoryRecordItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ItemInventoryRecordItemViewModel(itemModle, mContext);
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

