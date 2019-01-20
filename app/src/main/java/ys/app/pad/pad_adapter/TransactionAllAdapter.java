package ys.app.pad.pad_adapter;

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
import ys.app.pad.databinding.ItemTransactionBinding;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.viewmodel.ItemTransactionViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by admin on 2017/6/11.
 */

public class TransactionAllAdapter extends RecyclerView.Adapter<TransactionAllAdapter.MyViewHolder> {

    private final Context mContext;

    public static List<OrderInfo> getList() {
        return mList;
    }

    private static List<OrderInfo> mList;

    public static void setListener(OnItemClickListener listener) {
        TransactionAllAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public TransactionAllAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public TransactionAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTransactionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_transaction, parent, false);
        return new TransactionAllAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(TransactionAllAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<OrderInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(int startHttp, List<OrderInfo> list) {
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



    public static class MyViewHolder extends IViewHolder {

        private ItemTransactionBinding mBinding;

        public MyViewHolder(@NonNull ItemTransactionBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(OrderInfo itemModle, final int position, Context mContext) {
            ItemTransactionViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ItemTransactionViewModel(itemModle, mContext);
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
