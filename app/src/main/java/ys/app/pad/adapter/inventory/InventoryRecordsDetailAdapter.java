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
import ys.app.pad.databinding.InventoryRecordDetailBinding;
import ys.app.pad.model.InventoryRecordsInfo;
import ys.app.pad.viewmodel.inventory.InventoryRecordDetailViewModel;

/**
 * Created by aaa on 2017/3/17.
 */

public class InventoryRecordsDetailAdapter extends RecyclerView.Adapter<InventoryRecordsDetailAdapter.MyViewHolder> {

    private final Context mContext;
    private List<InventoryRecordsInfo.InventoryListBean> mList;

    public static void setListener(OnItemClickListener listener) {
        InventoryRecordsDetailAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public InventoryRecordsDetailAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public InventoryRecordsDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        InventoryRecordDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_inventory_record_detail, parent, false);
        return new InventoryRecordsDetailAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(InventoryRecordsDetailAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<InventoryRecordsInfo.InventoryListBean > list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private InventoryRecordDetailBinding mBinding;

        public MyViewHolder(@NonNull InventoryRecordDetailBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(InventoryRecordsInfo.InventoryListBean  itemModle, final int position, Context mContext) {
            InventoryRecordDetailViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new InventoryRecordDetailViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }

//            int result=itemModle.getInventoryNum()-itemModle.getStockNum();
//            if (result>0){
//                mBinding.tvRecordYingkui.setText("+");
//            }else if (result<0){
//                mBinding.tvRecordYingkui.setText("-");
//            }else{
//                mBinding.tvRecordYingkui.setText("");
//            }

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
