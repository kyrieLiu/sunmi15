package ys.app.pad.adapter.manage;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivity;
import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemVipCardBinding;
import ys.app.pad.model.VipCardInfo;
import ys.app.pad.viewmodel.manage.VipCardItemViewModel;

/**
 * Created by aaa on 2017/3/16.
 */

public class VipCardAdapter  extends RecyclerView.Adapter<VipCardAdapter.MyViewHolder> {

    private final BaseActivity mBaseActivity;
    private List<VipCardInfo> mList;
    private int type=0;//区别是对话框形式还是页面形式

    public  void setListener(OnItemClickListener listener) {
       this.listener = listener;
    }

    private  OnItemClickListener listener;
    private int classfication;

    public VipCardAdapter(BaseActivity activity,int classfication)
    {
        mList = new ArrayList<>();
        this.mBaseActivity = activity;
        this.classfication=classfication;
    }

    @Override
    public VipCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVipCardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mBaseActivity), R.layout.item_vip_card, parent, false);
        return new VipCardAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(VipCardAdapter.MyViewHolder holder, int position) {
        holder.bind(listener,mList.get(position), position,mBaseActivity,type,classfication);
    }

    public void setShowType(int type){
        this.type=type;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<VipCardInfo> list) {
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
        VipCardInfo item=mList.get(registerI);
        item.setState(3);
        mList.remove(registerI);
        mList.add(item);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public List<VipCardInfo> getList(){
        return  mList;
    }


    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private ItemVipCardBinding mBinding;

        public MyViewHolder(@NonNull ItemVipCardBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(final OnItemClickListener listener, VipCardInfo itemModle, final int position, BaseActivity mBaseActivity, int showType,int classification) {
            VipCardItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new VipCardItemViewModel(position,itemModle, mBaseActivity,showType,classification,mBinding);
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
                mBinding.btVipCardSelect.setOnClickListener(new View.OnClickListener() {
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

