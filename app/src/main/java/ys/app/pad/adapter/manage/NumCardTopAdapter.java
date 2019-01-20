package ys.app.pad.adapter.manage;

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
import ys.app.pad.itemmodel.NumCardEntityInfo;

/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class NumCardTopAdapter extends RecyclerView.Adapter<NumCardTopAdapter.MyViewHolder> {

    private final Context mContext;

    public List<NumCardEntityInfo> getList() {
        return mList;
    }

    private List<NumCardEntityInfo> mList;


    private OnItemClickListener listener;

    private LayoutInflater mInflater;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public NumCardTopAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public NumCardTopAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_title, parent, false);
        MyViewHolder viewHolder=new NumCardTopAdapter.MyViewHolder(view);
        viewHolder.tv_title= (TextView) view.findViewById(R.id.tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumCardTopAdapter.MyViewHolder holder, int position) {
        NumCardEntityInfo info=mList.get(position);
        holder.tv_title.setText(info.getName()+"  "+info.getNum()+"次");
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

    public void setList(List<NumCardEntityInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
