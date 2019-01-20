package ys.app.pad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;

/**
 * Created by admin on 2017/6/11.
 */

public class HairdressingInspectAdapter extends RecyclerView.Adapter<HairdressingInspectAdapter.MyViewHolder> {

    private final Context mContext;


    private LayoutInflater mInflater;
    public List<String> filterList=new ArrayList<>();
    private String[] hairDressings;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private  OnItemClickListener listener;

    public HairdressingInspectAdapter(Context context, String[] hairDressings)
    {
        this.hairDressings=hairDressings;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HairdressingInspectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_hairdressing_inspect, parent, false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        viewHolder.tv_title= (TextView) view.findViewById(R.id.tv_item_hairdressing_title);
        viewHolder.radioGroup= (RadioGroup) view.findViewById(R.id.rg_item_hairdressing_select);
        viewHolder.bt_true= (RadioButton) view.findViewById(R.id.rb_item_hairdressing_true);
        viewHolder.bt_false= (RadioButton) view.findViewById(R.id.rb_item_hairdressing_false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HairdressingInspectAdapter.MyViewHolder holder, final int position) {
        String title=hairDressings[position];
            holder.tv_title.setText(title);
            if ("毛量".equals(title)){
                holder.bt_true.setText("多");
                holder.bt_false.setText("少");
            }else if ("毛质".equals(title)){
                holder.bt_true.setText("软");
                holder.bt_false.setText("硬");
            }
            holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    String info=hairDressings[position];
                    switch (checkedId){
                        case R.id.rb_item_hairdressing_true:
                            filterList.add(info);
                            break;
                        case R.id.rb_item_hairdressing_false:
                            if (filterList.contains(info)){
                                filterList.remove(info);
                            }
                            break;
                    }
                }
            });
    }


    @Override
    public int getItemCount() {
        return hairDressings.length;
    }




       class MyViewHolder extends RecyclerView.ViewHolder {

         TextView tv_title;
         RadioGroup radioGroup;
         RadioButton bt_true,bt_false;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
