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
import ys.app.pad.databinding.ItemSearchServiceBinding;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.viewmodel.SearchServiceItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/6/9.
 */

public class SearchServiceAdapter extends RecyclerView.Adapter<SearchServiceAdapter.MyViewHolder> {

    private final Context mContext;

    public List<ServiceInfo> getList() {
        return mList;
    }

    private List<ServiceInfo> mList = new ArrayList<>();
    private static OnItemClickListener listener;

    public SearchServiceAdapter(Context context) {
        this.mContext = context;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SearchServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSearchServiceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search_service, parent, false);
        return new SearchServiceAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SearchServiceAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<ServiceInfo> list) {
        if (list != null && !list.isEmpty()) {
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void clearData() {
        this.mList.clear();
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemSearchServiceBinding mBinding;

        public MyViewHolder(@NonNull ItemSearchServiceBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(ServiceInfo itemModle, final int position, Context mContext) {
            SearchServiceItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new SearchServiceItemViewModel(itemModle, mContext);
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
