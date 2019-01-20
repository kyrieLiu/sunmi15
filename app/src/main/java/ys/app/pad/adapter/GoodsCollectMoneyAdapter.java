package ys.app.pad.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.callback.OnItemLongClickListener;
import ys.app.pad.databinding.ItemGoodsCollectBinding;
import ys.app.pad.model.GoodInfo;
import ys.app.pad.viewmodel.GoodsItemViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by lyy on 2017/2/14 11:21.
 * email：2898049851@qq.com
 */

public class GoodsCollectMoneyAdapter extends RecyclerView.Adapter<GoodsCollectMoneyAdapter.MyViewHolder> {

    private final Context mContext;

    public List<GoodInfo> getList() {
        return mList;
    }

    private  List<GoodInfo> mList = new ArrayList<>();
    private  OnItemLongClickListener longClicklistener;

    private  OnItemClickListener listener;

    public GoodsCollectMoneyAdapter(Context context)
    {
        this.mContext = context;
    }
    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClicklistener) {
        this.longClicklistener = longClicklistener;
    }

    @Override
    public GoodsCollectMoneyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemGoodsCollectBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_goods_collect_money, parent, false);
        return new GoodsCollectMoneyAdapter.MyViewHolder(binding,listener,longClicklistener);
    }

    @Override
    public void onBindViewHolder(GoodsCollectMoneyAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(int startHttp, List<GoodInfo> list) {
        if( 0 == startHttp){
            if (list != null ){
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
    }

    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if(registerI != mList.size()){
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }


    public static class MyViewHolder extends IViewHolder {

        private final OnItemClickListener mListenner;
        private final OnItemLongClickListener mLongListenner;
        private ItemGoodsCollectBinding mBinding;

        public MyViewHolder(@NonNull ItemGoodsCollectBinding binding,OnItemClickListener listener, OnItemLongClickListener longClicklistener) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mListenner = listener;
            this.mLongListenner = longClicklistener;
        }


        public void bind(GoodInfo itemModle, final int position, Context mContext) {
            GoodsItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new GoodsItemViewModel(itemModle, mContext,-1);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle,-1);
            }
            if (itemModle.getIsPromotion()==0){
                mBinding.llPromotionTime.setVisibility(View.GONE);
            }else{
                mBinding.llPromotionTime.setVisibility(View.VISIBLE);
            }
            Glide.with(mContext).load(itemModle.getImgpath()).placeholder(R.mipmap.dog).into(mBinding.ivItemCollectPicture);
            if(mBinding!=null){
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListenner != null){
                            mListenner.onItemClick(root, position);
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
