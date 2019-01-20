package ys.app.pad.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.itemmodel.CardNumberList2Bean;


/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class ServiceNumCardChildAdapter extends RecyclerView.Adapter<ServiceNumCardChildAdapter.MyViewHolder> {

    private final Context mContext;

    public List<CardNumberList2Bean> getList() {
        return mList;
    }

    private List<CardNumberList2Bean> mList;


    private OnItemClickListener listener;

    private LayoutInflater mInflater;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ServiceNumCardChildAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ServiceNumCardChildAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_service_num_card, parent, false);
        MyViewHolder viewHolder=new ServiceNumCardChildAdapter.MyViewHolder(view);
        viewHolder.tv_name= (TextView) view.findViewById(R.id.tv_item_service_num_name);
        viewHolder.tv_num= (TextView) view.findViewById(R.id.tv_item_service_num_number);
        viewHolder.cardView= (CardView) view.findViewById(R.id.root_view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceNumCardChildAdapter.MyViewHolder holder, final int position) {
        CardNumberList2Bean info=mList.get(position);
        holder.tv_name.setText(info.getProductName());
        holder.tv_num.setText("服务次数: "+info.getNumber());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v,position);
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

    public void setList(List<CardNumberList2Bean> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_num;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
