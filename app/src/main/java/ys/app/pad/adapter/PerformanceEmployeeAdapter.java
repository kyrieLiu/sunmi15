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
import ys.app.pad.model.EmployeeInfo;

/**
 * Created by aaa on 2017/5/2.
 */

public class PerformanceEmployeeAdapter extends RecyclerView.Adapter<PerformanceEmployeeAdapter.MyViewHolder> {

    private final Context mContext;
    private List<EmployeeInfo> mList;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private static OnItemClickListener listener;

    private LayoutInflater inflater;

    public PerformanceEmployeeAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public PerformanceEmployeeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate( R.layout.item_performance_employee, parent, false);
        MyViewHolder holder=new MyViewHolder(view);
        holder.tv_name= (TextView) view.findViewById(R.id.tv_item_performance_employee_name);
        return holder;
    }

    @Override
    public void onBindViewHolder(final PerformanceEmployeeAdapter.MyViewHolder holder, final int position) {
        holder.tv_name.setText(mList.get(position).getName());
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.onItemClick(holder.tv_name,position);
                }
            }
        });
        if (1 == mList.get(position).getSelect()) {
            holder.tv_name.setBackground(mContext.getResources().getDrawable(R.drawable.shape_circle_blue));
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.tv_name.setBackground(mContext.getResources().getDrawable(R.drawable.shape_circle_white));
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.blue));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<EmployeeInfo> list) {
        if (mList == null) return;
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private View view;
        private TextView tv_name;


        public MyViewHolder(View view) {
            super(view);
            this.view=view;
        }
    }
}
