package ys.app.pad.adapter.inventory;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.databinding.ItemOutPutBinding;
import ys.app.pad.fragment.OutPutInventoryFragment;
import ys.app.pad.itemmodel.OutPutItemModel;
import ys.app.pad.model.OutStorageInfo;

/**
 * Created by liuyin on 2017/9/11.
 */

public class OutPutInventoryAdapter extends RecyclerView.Adapter<OutPutInventoryAdapter.MyViewHodler>{
    private List<OutStorageInfo> mList;
    private OutPutInventoryFragment fragment;
    private int type;
    public OutPutInventoryAdapter(OutPutInventoryFragment fragment, int type){
        this.mList=new ArrayList<>();
        this.fragment=fragment;
        this.type=type;
    }

    @Override
    public MyViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemOutPutBinding binding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_out_put_inventory,parent,false);
        return new MyViewHodler(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHodler holder, int position) {
        holder.bind(mList.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(int startHttp, List<OutStorageInfo> list) {
        if( 0 == startHttp){
            mList.clear();
        }

        int positionStart = mList.size();
        int itemCount = list.size();
        mList.addAll(list);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }
    }

     class MyViewHodler extends RecyclerView.ViewHolder{
         ItemOutPutBinding binding;
        public MyViewHodler(ItemOutPutBinding binding) {
            super(binding.rootView);
            this.binding=binding;
        }
        public void bind(OutStorageInfo bean,int position){

            OutPutItemModel model;
            if (binding.getItemViewModel()==null){
                model=new OutPutItemModel(bean,fragment,type);
                model.setModel(bean);
                binding.setItemViewModel(model);
            }else{
                model=binding.getItemViewModel();
                model.setModel(bean);
            }
        }
    }
}
