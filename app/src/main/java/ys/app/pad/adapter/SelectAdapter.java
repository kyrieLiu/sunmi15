package ys.app.pad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.BaseSelect;
import ys.app.pad.model.SelectInfo;

/**
 * Created by aaa on 2017/3/20.
 */

public abstract class SelectAdapter<T extends BaseSelect> extends RecyclerView.Adapter<SelectAdapter.MyViewHolder> {

    private final Context mActivity;
    protected List<T> mList = new ArrayList<>();
    private int lastSelectPos = -1;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;


    public SelectAdapter(Context activity, List<T> list) {
        this.mActivity = activity;
        this.mList.clear();
        this.mList.addAll(list);
    }


    @Override
    public void onBindViewHolder(SelectAdapter.MyViewHolder holder, int position) {
        onBindView(holder, position);
    }

    public abstract void onBindView(SelectAdapter.MyViewHolder holder, int position);

    @Override
    public SelectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mActivity)
                .inflate(R.layout.item_select, parent, false);
        return new SelectAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }


    public int getPosition() {
        return lastSelectPos;
    }

    public void setData(List<T> list) {
        this.mList.clear();
        this.mList.addAll(list);
    }
    public SelectInfo getObject(){
        return (SelectInfo) mList.get(lastSelectPos);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        ImageView checkIv;


        public MyViewHolder(final View itemView) {
            super(itemView);

            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            checkIv = (ImageView) itemView.findViewById(R.id.check_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectPos = getAdapterPosition();
                    if (-1 != lastSelectPos) {
                        mList.get(lastSelectPos).setSelect(false);
                    }else{
                        mList.get(selectPos).setSelect(true);
                    }
                    lastSelectPos = selectPos;
                    if (listener != null) {
                        listener.onItemClick(itemView, lastSelectPos);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        public void setName(String s) {
            if (null == nameTv) return;
            nameTv.setText(s);
        }

        public void setSelect(boolean isSelect) {
            checkIv.setSelected(isSelect);
        }
    }
}
