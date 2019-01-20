package ys.app.pad.adapter.manage;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemNumCardListBinding;
import ys.app.pad.itemmodel.NumCardItemViewModel;
import ys.app.pad.model.NumCardListInfo;

/**
 * Created by aaa on 2017/3/16.
 */

public class NumCardListAdapter extends RecyclerView.Adapter<NumCardListAdapter.MyViewHolder> {

    private final BaseActivity mBaseActivity;
    private List<NumCardListInfo> mList;
    RecyclerView parentRecyclerView;

    public static void setListener(OnItemClickListener listener) {
        NumCardListAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public NumCardListAdapter(BaseActivity activity,RecyclerView parentRecyclerView)
    {
        mList = new ArrayList<>();
        this.mBaseActivity = activity;
        this.parentRecyclerView = parentRecyclerView;
    }

    @Override
    public NumCardListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemNumCardListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mBaseActivity), R.layout.item_num_card_list, parent, false);
        return new NumCardListAdapter.MyViewHolder(binding, parentRecyclerView);
    }

    @Override
    public void onBindViewHolder(NumCardListAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mBaseActivity);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<NumCardListInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }
    public void updateSort(int registerI) {
        NumCardListInfo item=mList.get(registerI);
        item.setState(3);
        mList.remove(registerI);
        mList.add(item);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private ItemNumCardListBinding mBinding;
        private ImageView iv_show_item;

        public MyViewHolder(@NonNull ItemNumCardListBinding binding,RecyclerView parent) {
            super(binding.rootView);
            this.mBinding = binding;
            mBinding.childRecyclerView.setNestParent(parent);
            this.iv_show_item = mBinding.ivShowItem;
        }

        public void bind(NumCardListInfo itemModle, final int position, BaseActivity mBaseActivity) {
            NumCardItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new NumCardItemViewModel(position,itemModle, mBaseActivity,mBinding);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
                mBinding.setItemViewModel(itemViewModel);
            }
            if (itemModle.getState()==3){
                mBinding.btVipCardCancellation.setBackgroundDrawable(mBaseActivity.getResources().getDrawable(R.drawable.shape_button_grey));
                mBinding.btVipCardCancellation.setTextColor(mBaseActivity.getResources().getColor(R.color.white));
                mBinding.btVipCardCancellation.setText("已作废");
            }else{
                mBinding.btVipCardCancellation.setBackgroundDrawable(mBaseActivity.getResources().getDrawable(R.drawable.selector_press_button_empty));
                mBinding.btVipCardCancellation.setTextColor(mBaseActivity.getResources().getColor(R.color.selector_press_button_text_color));
                mBinding.btVipCardCancellation.setText("作废");
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

