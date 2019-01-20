package ys.app.pad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemAddEmployeeBinding;
import ys.app.pad.model.AddEmployeeInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.WorkTypeInfo;
import ys.app.pad.viewmodel.manage.AddEmployeeItemViewModel;

/**
 * Created by aaa on 2017/6/6.
 */
public class AddEmployeeAdapter extends RecyclerView.Adapter<AddEmployeeAdapter.MyViewHolder> {

    private final Context mContext;
    private List<AddEmployeeInfo> mList;
    private static BaseListResult<WorkTypeInfo> mBeanList;

    public static void setListener(OnItemClickListener listener) {
        AddEmployeeAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public AddEmployeeAdapter(Context context, List<AddEmployeeInfo> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public AddEmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAddEmployeeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_add_employee, parent, false);
        return new AddEmployeeAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AddEmployeeAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setWorkType(BaseListResult<WorkTypeInfo> data){
        mBeanList = data;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ItemAddEmployeeBinding mBinding;

        public MyViewHolder(@NonNull ItemAddEmployeeBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(AddEmployeeInfo itemModle, final int position, Context mContext) {
            AddEmployeeItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new AddEmployeeItemViewModel(itemModle, mContext,mBeanList);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,mBeanList);
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
