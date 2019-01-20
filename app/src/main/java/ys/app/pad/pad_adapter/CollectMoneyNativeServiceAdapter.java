package ys.app.pad.pad_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.ServiceTypeInfo;

/**
 * Created by liuyin on 2017/7/20.
 */

public class CollectMoneyNativeServiceAdapter extends RecyclerView.Adapter<CollectMoneyNativeServiceAdapter.MyViewHolder> {

    private List<ServiceTypeInfo> list;
    private Context context;
    private  OnItemClickListener listener;
    private LayoutInflater inflater;


    public CollectMoneyNativeServiceAdapter(List<ServiceTypeInfo> list, Context context){
        this.list=list;
        this.context=context;
        inflater = LayoutInflater.from(context);
    }
    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_layout_collect_money_navicat,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ServiceTypeInfo info = list.get(position);
         holder.setTitle(position, info.getName());
         holder.isSelect(info.getSelect());
        holder.rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

         private TextView tv_title;
        private RelativeLayout rl_bg;
        public MyViewHolder(View view) {
           super(view);
            tv_title=(TextView)view.findViewById(R.id.tv_item_collect_money_title);
            rl_bg=(RelativeLayout)view.findViewById(R.id.rl_item_goods);

        }
       private void setTitle(int position,String name){
           tv_title.setText(name);
       }
         public void isSelect(int select) {
             if (1 == select) {
                 rl_bg.setBackgroundColor(context.getResources().getColor(R.color.color_main));
             } else {
                 rl_bg.setBackgroundResource(0);
             }
         }
    }
}
