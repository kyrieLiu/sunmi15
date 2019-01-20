package ys.app.pad.adapter;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.databinding.EmployeeServiceBinding;
import ys.app.pad.model.SummitOrderInfo;
import ys.app.pad.viewmodel.ShoppingCarAdapterViewModel;

/**
 * 作者：lv
 * 时间：2017/4/3 18:30
 */

public class ShoppingCarAdapter extends RecyclerView.Adapter<ShoppingCarAdapter.MyViewHolder> {


    private Activity mContext;
    private List<SummitOrderInfo> mList;
    public ShoppingCarAdapter(Activity context){
        this.mContext = context;
        this.mList = new ArrayList<>();
    }

    @Override
    public ShoppingCarAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EmployeeServiceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_employee_shop_car_goods, parent, false);
        return new ShoppingCarAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ShoppingCarAdapter.MyViewHolder holder, int position) {
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
        public EmployeeServiceBinding mBinding;

        public MyViewHolder(EmployeeServiceBinding binding) {
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
        }
    }
}