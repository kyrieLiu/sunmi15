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
import ys.app.pad.databinding.ItemGoodsBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.GoodsItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by Administrator on 2017/6/10.
 */

public class GoodsSearchAdapter extends RecyclerView.Adapter<GoodsSearchAdapter.MyViewHolder> {
    private final Context mContext;
    private int intentFrom;

    public List<GoodInfo> getList() {
        return mList;
    }

    private  List<GoodInfo> mList = new ArrayList<>();
    private  OnItemClickListener listener;
    private OnItemLongClickListener longClicklistener;

    public GoodsSearchAdapter(Context context,int intentFrom)
    {
        this.mContext = context;
        this.intentFrom=intentFrom;
    }

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClicklistener) {
        this.longClicklistener = longClicklistener;
    }

    @Override
    public GoodsSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGoodsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_goods, parent, false);
        return new GoodsSearchAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(GoodsSearchAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext,listener,longClicklistener,intentFrom);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(int startHttp, List<GoodInfo> list) {
        if( 0 == startHttp){
            if (list != null && !list.isEmpty()){
                this.mList.clear();
                this.mList.addAll(list);
                notifyDataSetChanged();
            }
        }else if (startHttp > 0){
            if (list != null && !list.isEmpty() ){
                this.mList.addAll(list);
                notifyItemRangeInserted(this.mList.size() - list.size() , list.size());
            }
        }

//        int positionStart = list.size();
//        int itemCount = list.size();

//        if (positionStart > 0 && itemCount > 0) {
//            notifyItemRangeInserted(positionStart, itemCount);
//        } else {
//        }


    }
    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    public void setList(List<GoodInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public void clearData() {
        this.mList.clear();
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends IViewHolder {

        private ItemGoodsBinding mBinding;

        public MyViewHolder(@NonNull ItemGoodsBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(GoodInfo itemModle, final int position, Context mContext, final OnItemClickListener listener, final OnItemLongClickListener mLongListenner,int intentFrom) {
            GoodsItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new GoodsItemViewModel(itemModle, mContext,intentFrom);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,intentFrom);
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
                        if (mLongListenner != null) {
                            mLongListenner.onItemLongClick(root, position);
                        }
                        return true;
                    }
                });
            }
        }
    }
}


