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
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ItemEmployeeListBinding;
import ys.app.pad.model.EmployeeInfo;
import ys.app.pad.viewmodel.manage.EmployeeListItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/6/5.
 */

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.MyViewHolder> {

    private final Context mContext;

    public List<EmployeeInfo> getList() {
        return mList;
    }

    private List<EmployeeInfo> mList;

    private static OnItemClickListener listener;
    private static OnItemLongClickListener longClicklistener;

    public EmployeeListAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClicklistener) {
        this.longClicklistener = longClicklistener;
    }

    @Override
    public EmployeeListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemEmployeeListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_employee_list, parent, false);
        return new EmployeeListAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(EmployeeListAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public void setList(int startHttp, List<EmployeeInfo> list) {
        if (0 == startHttp) {
            mList.clear();
        }

        int positionStart = mList.size();
        int itemCount = list.size();
        mList.addAll(list);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }

    }


    public static class MyViewHolder extends IViewHolder {

        private ItemEmployeeListBinding mBinding;

        public MyViewHolder(@NonNull ItemEmployeeListBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(EmployeeInfo itemModle, final int position, Context mContext) {
            EmployeeListItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new EmployeeListItemViewModel(itemModle, mContext);
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
                root.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (longClicklistener != null) {
                            longClicklistener.onItemLongClick(root, position);
                        }
                        return true;
                    }
                });
            }

        }
    }
}
