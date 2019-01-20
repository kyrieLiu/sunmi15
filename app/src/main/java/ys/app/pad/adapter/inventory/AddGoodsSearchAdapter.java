package ys.app.pad.adapter.inventory;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.GoodInfo;

/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class AddGoodsSearchAdapter extends RecyclerView.Adapter<AddGoodsSearchAdapter.MyViewHolder> {

    private final Context mContext;

    public List<GoodInfo> getList() {
        return mList;
    }

    private List<GoodInfo> mList;


    private OnItemClickListener listener;

    private LayoutInflater mInflater;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public AddGoodsSearchAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AddGoodsSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_goods_name, parent, false);
        MyViewHolder viewHolder=new AddGoodsSearchAdapter.MyViewHolder(view);
        viewHolder.tv_name=  (TextView) view.findViewById(R.id.tv_item_good_name);
        viewHolder.rl_name= (RelativeLayout) view.findViewById(R.id.rl_item_add_goods);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddGoodsSearchAdapter.MyViewHolder holder, final int position) {
        GoodInfo info=mList.get(position);
        holder.tv_name.setText(info.getName());
        holder.rl_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(v,position);
                }
            }
        });
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

    public void setList(List<GoodInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }
    public GoodInfo getItem(int position){
        return mList.get(position);
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        RelativeLayout rl_name;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
