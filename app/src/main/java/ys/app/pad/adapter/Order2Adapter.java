package ys.app.pad.adapter;

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
import ys.app.pad.databinding.ItemOrder2Binding;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.viewmodel.Order2ItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/4/26.
 */

public class Order2Adapter extends RecyclerView.Adapter<Order2Adapter.MyViewHolder> {

    private final BaseActivity mBaseActivity;

    public List<OrderInfo> getList() {
        return mList;
    }

    private  List<OrderInfo> mList = new ArrayList<>();
    private static OnItemClickListener listener;

    public Order2Adapter(BaseActivity context)
    {
        this.mBaseActivity = context;
    }

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Order2Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOrder2Binding binding = DataBindingUtil.inflate(LayoutInflater.from(mBaseActivity), R.layout.item_order2, parent, false);
        return new Order2Adapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(Order2Adapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mBaseActivity);
    }


    @Override
    public int getItemCount() {
        return mList.size();
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


    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemOrder2Binding mBinding;

        public MyViewHolder(@NonNull ItemOrder2Binding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(OrderInfo itemModle, final int position, BaseActivity mBaseActivity) {
            Order2ItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new Order2ItemViewModel(position,itemModle, mBaseActivity);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,position);
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

