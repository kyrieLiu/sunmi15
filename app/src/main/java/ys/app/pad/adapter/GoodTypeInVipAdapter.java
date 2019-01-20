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
import ys.app.pad.databinding.ItemGoodTypeInVipBinding;
import ys.app.pad.model.GoodTypeInfo;
import ys.app.pad.viewmodel.GoodTypeInVipItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by admin on 2017/7/5.
 */

public class GoodTypeInVipAdapter extends RecyclerView.Adapter<GoodTypeInVipAdapter.MyViewHolder> {

    private final Context mContext;
    private final boolean mIsInput;
    private final boolean mIsAddVipCard;

    public List<GoodTypeInfo> getList() {
        return mList;
    }

    private  List<GoodTypeInfo> mList = new ArrayList<>();
    private static OnItemClickListener listener;

    public GoodTypeInVipAdapter(Context context,boolean isInput,boolean isAddVipCard)
    {
        this.mContext = context;
        this.mIsInput = isInput;
        this.mIsAddVipCard = isAddVipCard;
    }

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public GoodTypeInVipAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGoodTypeInVipBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_good_type_in_vip, parent, false);
        return new GoodTypeInVipAdapter.MyViewHolder(binding,mIsInput,mIsAddVipCard);
    }

    @Override
    public void onBindViewHolder(GoodTypeInVipAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList( List<GoodTypeInfo> list) {
        if (list != null && !list.isEmpty()){
            this.mList.clear();
            this.mList.addAll(list);
            notifyDataSetChanged();
        }
    }



    public static class MyViewHolder extends IViewHolder {

        private ItemGoodTypeInVipBinding mBinding;
        private  boolean mIsInput;
        private  boolean mIsAddVipCard;

        public MyViewHolder(@NonNull ItemGoodTypeInVipBinding binding,boolean isInput,boolean isAddVipCard) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mIsInput = isInput;
            this.mIsAddVipCard = isAddVipCard;
        }

        public void bind(GoodTypeInfo itemModle, final int position, Context mContext) {
            GoodTypeInVipItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new GoodTypeInVipItemViewModel(itemModle, mContext,mIsInput,mIsAddVipCard);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,mIsInput,mIsAddVipCard);
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
