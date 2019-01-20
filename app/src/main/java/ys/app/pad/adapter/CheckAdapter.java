package ys.app.pad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.databinding.ItemCheckBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.CheckItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by aaa on 2017/3/7.
 */

public class CheckAdapter  extends RecyclerView.Adapter<CheckAdapter.MyViewHolder> {

    private final Context mContext;

    public static List<GoodInfo> getList() {
        return mList;
    }

    private static List<GoodInfo> mList;


    public CheckAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }


    @Override
    public CheckAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemCheckBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_check, parent, false);
        return new CheckAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CheckAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<GoodInfo> list) {
//        if( 0 == startHttp){
//            list.clear();
//        }
//
//        int positionStart = list.size();
//        int itemCount = list.size();
//        list.addAll(list);
//        if (positionStart > 0 && itemCount > 0) {
//            notifyItemRangeInserted(positionStart, itemCount);
//        } else {
//            notifyDataSetChanged();
//        }

        this.mList.clear();
        this.mList.addAll(list);
        notifyDataSetChanged();

    }
    public void setScanList(List<GoodInfo> list,int scanCount){
        if (scanCount==0){
            mList.clear();
        }
        int positionStart = mList.size();
        int itemCount = 0;
        for (GoodInfo info:list){
            if (!mList.contains(info)){
                info.setInventoryNum(info.getStockNum());
                mList.add(info);
                itemCount++;
            }
        }
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else if (itemCount!=0){
            notifyDataSetChanged();
        }
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemCheckBinding mBinding;

        public MyViewHolder(@NonNull ItemCheckBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(GoodInfo itemModle, final int position, Context mContext) {
            CheckItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new CheckItemViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }


        }
    }
}
