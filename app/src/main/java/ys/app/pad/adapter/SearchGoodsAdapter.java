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
import ys.app.pad.databinding.ItemSearchGoodsBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.SearchGoodsItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/6/9.
 */

public class SearchGoodsAdapter extends RecyclerView.Adapter<SearchGoodsAdapter.MyViewHolder> {

    private final Context mContext;

    public List<GoodInfo> getList() {
        return mList;
    }

    private List<GoodInfo> mList = new ArrayList<>();
    private static OnItemClickListener listener;

    public SearchGoodsAdapter(Context context) {
        this.mContext = context;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public SearchGoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSearchGoodsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search_goods, parent, false);
        return new SearchGoodsAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SearchGoodsAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<GoodInfo> list) {
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

        private ItemSearchGoodsBinding mBinding;

        public MyViewHolder(@NonNull ItemSearchGoodsBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(GoodInfo itemModle, final int position, Context mContext) {
            SearchGoodsItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new SearchGoodsItemViewModel(itemModle, mContext);
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
