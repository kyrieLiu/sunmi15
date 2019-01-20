package ys.app.pad.adapter.manage;

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
import ys.app.pad.databinding.ItemEmployeePerformance1Binding;
import ys.app.pad.model.EmployeePerformanceInfo;
import ys.app.pad.viewmodel.manage.EmployeePerformance1ItemViewModel;

/**
 * Created by aaa on 2017/5/2.
 */

public class EmployeePerformance1Adapter extends RecyclerView.Adapter<EmployeePerformance1Adapter.MyViewHolder> {

    private final Context mContext;
    private List<EmployeePerformanceInfo.OrderDetailedListCommodityBean> mList;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static OnItemClickListener listener;

    public EmployeePerformance1Adapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public EmployeePerformance1Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEmployeePerformance1Binding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_employee_performance_1, parent, false);
        return new EmployeePerformance1Adapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(EmployeePerformance1Adapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<EmployeePerformanceInfo.OrderDetailedListCommodityBean> list) {
        if (mList == null) return;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemEmployeePerformance1Binding mBinding;

        public MyViewHolder(@NonNull ItemEmployeePerformance1Binding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(EmployeePerformanceInfo.OrderDetailedListCommodityBean itemModle, final int position, Context mContext) {
            EmployeePerformance1ItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new EmployeePerformance1ItemViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }

            if (mBinding != null) {
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClick(root, position);
                        }
                    }
                });
            }
        }
    }
}
