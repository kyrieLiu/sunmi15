package ys.app.pad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.databinding.ItemTransactionDetailBinding;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.viewmodel.ItemTransactionDetailViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by admin on 2017/6/11.
 */

public class TransactionDetailAdapter extends RecyclerView.Adapter<TransactionDetailAdapter.MyViewHolder> {

    private  Context mContext;
    private OrderInfo mOrderInfo;

    public static List<OrderInfo.OrderDetailedListBean> getList() {
        return mList;
    }

    private static List<OrderInfo.OrderDetailedListBean> mList;


    public TransactionDetailAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }


    @Override
    public TransactionDetailAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTransactionDetailBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_transaction_detail, parent, false);
        return new TransactionDetailAdapter.MyViewHolder(binding,mOrderInfo);
    }

    @Override
    public void onBindViewHolder(TransactionDetailAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(int startHttp, List<OrderInfo.OrderDetailedListBean> list) {
        if( 0 == startHttp){
            if (list != null && !list.isEmpty()){
                this.mList.clear();
                this.mList.addAll(list);
                notifyDataSetChanged();
            }
        }else if (startHttp > 0){
            if (list != null && !list.isEmpty() ){
                this.mList.addAll(list);
                notifyItemRangeInserted(this.mList.size() - list.size() , list.size());
            }
        }

    }

    public void setOrderInfo(OrderInfo info) {
        this.mOrderInfo = info;
    }


    public static class MyViewHolder extends IViewHolder {


        private ItemTransactionDetailBinding mBinding;
        private OrderInfo mInfo;


        public MyViewHolder(@NonNull ItemTransactionDetailBinding binding,OrderInfo info) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mInfo = info;
        }

        public void bind(OrderInfo.OrderDetailedListBean itemModle, final int position, Context mContext) {
            ItemTransactionDetailViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ItemTransactionDetailViewModel(itemModle, mContext,mInfo,position);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,mInfo,position);
            }

        }
    }
}
