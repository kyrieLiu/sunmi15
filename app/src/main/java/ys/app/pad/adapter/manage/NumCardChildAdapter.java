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
import ys.app.pad.databinding.ItemNumCardBinding;
import ys.app.pad.itemmodel.CardNumberList2Bean;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by admin on 2017/7/5.
 */

public class NumCardChildAdapter extends RecyclerView.Adapter<NumCardChildAdapter.MyViewHolder> {
    private final Context mContext;

    public List<CardNumberList2Bean> getList() {
        return mList;
    }

    private List<CardNumberList2Bean> mList;

    private OnItemClickListener listener;


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public NumCardChildAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;


    }

    @Override
    public NumCardChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNumCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_num_card_service, parent, false);
        return new NumCardChildAdapter.MyViewHolder(binding,  listener);
    }

    @Override
    public void onBindViewHolder(NumCardChildAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if (registerI != mList.size()) {
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public void setList(List<CardNumberList2Bean> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends IViewHolder {

        private ItemNumCardBinding mBinding;
        private final OnItemClickListener mListenner;
        public MyViewHolder(@NonNull ItemNumCardBinding binding, OnItemClickListener listener) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mListenner = listener;
        }

        public void bind(CardNumberList2Bean itemModle, final int position, Context mContext) {

            mBinding.tvItemNumCardName.setText(itemModle.getProductName());
            mBinding.etItemNumCardNumber.setText(itemModle.getNumber()+"");
            mBinding.etItemNumCardNumber.setEnabled(false);


            if (mBinding != null) {
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListenner != null) {
                            mListenner.onItemClick(root, position);
                        }
                    }
                });

            }
        }

    }
}

