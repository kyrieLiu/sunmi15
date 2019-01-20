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
import ys.app.pad.databinding.ItemEmployeeNameNewBinding;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.viewmodel.manage.EmployeeNameNewItemViewModel;

/**
 * Created by aaa on 2017/5/2.
 */

public class EmployeeNameNewAdapter extends RecyclerView.Adapter<EmployeeNameNewAdapter.MyViewHolder> {

    private final Context mContext;
    private List<EmployeeInfo> mList;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static OnItemClickListener listener;

    public EmployeeNameNewAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public EmployeeNameNewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEmployeeNameNewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_employee_name_new, parent, false);
        return new EmployeeNameNewAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(EmployeeNameNewAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<EmployeeInfo> list) {
        if (mList == null) return;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemEmployeeNameNewBinding mBinding;

        public MyViewHolder(@NonNull ItemEmployeeNameNewBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(EmployeeInfo itemModle, final int position, Context mContext) {
            EmployeeNameNewItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new EmployeeNameNewItemViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }
            if (itemModle.getSex()==1){
                mBinding.ivItemAchivementEmployee.setImageResource(R.mipmap.employee_woman);
            }else{
                mBinding.ivItemAchivementEmployee.setImageResource(R.mipmap.employee_man);
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
