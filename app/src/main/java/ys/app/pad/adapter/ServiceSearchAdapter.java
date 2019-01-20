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
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ItemServiceBinding;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.viewmodel.ServiceItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by Administrator on 2017/6/10.
 */

public class ServiceSearchAdapter extends RecyclerView.Adapter<ServiceSearchAdapter.MyViewHolder> {

    private final Context mContext;
    private boolean mPromotionSetting;

    public  List<ServiceInfo> getList() {
        return mList;
    }

    private static List<ServiceInfo> mList;

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private  OnItemClickListener listener;
    private OnItemLongClickListener longClicklistener;

    public ServiceSearchAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public ServiceSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemServiceBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_service, parent, false);
        return new ServiceSearchAdapter.MyViewHolder(binding,mPromotionSetting);
    }

    @Override
    public void onBindViewHolder(ServiceSearchAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext,listener,longClicklistener);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClicklistener) {
        this.longClicklistener = longClicklistener;
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<ServiceInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.mList.clear();
        notifyDataSetChanged();
    }
    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public void setPromot(boolean promotionSetting) {
        mPromotionSetting = promotionSetting;
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemServiceBinding mBinding;
        private boolean mPromotionSetting;

        public MyViewHolder(@NonNull ItemServiceBinding binding,boolean promotionSetting) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mPromotionSetting = promotionSetting;
        }

        public void bind(ServiceInfo itemModle, final int position, Context mContext, final OnItemClickListener listener, final OnItemLongClickListener longClicklistener) {
            ServiceItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ServiceItemViewModel(itemModle, mContext,mPromotionSetting);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,mPromotionSetting);
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
