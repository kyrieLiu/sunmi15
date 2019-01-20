package ys.app.pad.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.databinding.OrderListScreenBinding;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.viewmodel.ShoppingCarAdapterViewModel;

/**
 * 作者：lv
 * 时间：2017/4/3 18:30
 */

public class ShoppingCarViceAdapter extends RecyclerView.Adapter<ShoppingCarViceAdapter.MyViewHolder> {


    private Activity mContext;
    private List<SummitOrderInfo> mList;
    public ShoppingCarViceAdapter(Activity context){
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    @Override
    public ShoppingCarViceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        OrderListScreenBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_vice_order_list, parent, false);
        return new ShoppingCarViceAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ShoppingCarViceAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position),position,mContext);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setList(List<SummitOrderInfo> list) {
        if (list != null && !list.isEmpty()){
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        public OrderListScreenBinding mBinding;

        public MyViewHolder(OrderListScreenBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(SummitOrderInfo result,int position,Activity context){
            ShoppingCarAdapterViewModel viewModel;
            if (mBinding.getItemViewModel() == null){
                viewModel = new ShoppingCarAdapterViewModel(context,result);
                mBinding.setItemViewModel(viewModel);
            }else {
                viewModel = mBinding.getItemViewModel();
                viewModel.setModel(result);
            }
            if (position%2==0){
                mBinding.rootView.setBackgroundColor(context.getResources().getColor(R.color.order_bg));
            }else{
                mBinding.rootView.setBackgroundColor(context.getResources().getColor(R.color.order_item_bg));
            }
        }
    }
}