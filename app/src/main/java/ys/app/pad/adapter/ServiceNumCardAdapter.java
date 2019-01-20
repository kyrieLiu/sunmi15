package ys.app.pad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.NumCardListInfo;


/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class ServiceNumCardAdapter extends RecyclerView.Adapter<ServiceNumCardAdapter.MyViewHolder> {

    private final Context mContext;

    public List<NumCardListInfo> getList() {
        return mList;
    }

    private List<NumCardListInfo> mList;


    private OnItemClickListener listener;

    private LayoutInflater mInflater;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ServiceNumCardAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ServiceNumCardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_service_card, parent, false);
        MyViewHolder viewHolder=new ServiceNumCardAdapter.MyViewHolder(view);
        viewHolder.tv_name= (TextView) view.findViewById(R.id.tv_item_card_name);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceNumCardAdapter.MyViewHolder holder, final int position) {
        NumCardListInfo info=mList.get(position);
        holder.tv_name.setText(info.getName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
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

    public void setList(List<NumCardListInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
