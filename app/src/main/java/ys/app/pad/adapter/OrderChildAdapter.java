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
import ys.app.pad.databinding.ItemChildOrderBinding;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.viewmodel.OrderChildItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/4/26.
 */

public class OrderChildAdapter extends RecyclerView.Adapter<OrderChildAdapter.MyViewHolder> {

    private final Context mContext;

    public List<OrderInfo.OrderDetailedListBean> getList() {
        return mList;
    }

    private  List<OrderInfo.OrderDetailedListBean> mList = new ArrayList<>();
    private static OnItemClickListener listener;

    public OrderChildAdapter(Context context)
    {
        this.mContext = context;
    }

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public OrderChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemChildOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_child_order, parent, false);
        return new OrderChildAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(OrderChildAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList( List<OrderInfo.OrderDetailedListBean> list) {
        if (list != null && !list.isEmpty()){
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }



    public static class MyViewHolder extends IViewHolder {

        private ItemChildOrderBinding mBinding;

        public MyViewHolder(@NonNull ItemChildOrderBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(OrderInfo.OrderDetailedListBean itemModle, final int position, Context mContext) {
            OrderChildItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new OrderChildItemViewModel(itemModle, mContext);
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