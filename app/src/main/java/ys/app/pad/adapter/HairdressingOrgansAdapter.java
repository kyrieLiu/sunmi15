package ys.app.pad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;

/**
 * Created by admin on 2017/6/11.
 */

public class HairdressingOrgansAdapter extends RecyclerView.Adapter<HairdressingOrgansAdapter.MyViewHolder> {

    private final Context mContext;


    private LayoutInflater mInflater;
    public List<String> filterList=new ArrayList<>();
    private String[] hairDressings;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private  OnItemClickListener listener;

    public HairdressingOrgansAdapter(Context context, String[] hairDressings)
    {
        this.hairDressings=hairDressings;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public HairdressingOrgansAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_hairdressing_organs, parent, false);
        MyViewHolder viewHolder=new MyViewHolder(view);
        viewHolder.tv_title= (TextView) view.findViewById(R.id.tv_item_hairdressing_organ_title);
        viewHolder.checkBox= (CheckBox) view.findViewById(R.id.cb_item_hairdressing_organ);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HairdressingOrgansAdapter.MyViewHolder holder, final int position) {
            holder.tv_title.setText(hairDressings[position]);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    String info=hairDressings[position];
                    if (isChecked){
                        filterList.add(info);
                    }else{
                        if (filterList.contains(info)){
                            filterList.remove(info);
                        }
                    }
                }
            });
    }


    @Override
    public int getItemCount() {
        return hairDressings.length;
    }


//    public void setList(List<HairDressingInfo> list) {
//        mList.clear();
//        mList.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    public void setList(int startHttp, List<HairDressingInfo> list) {
//        if( 0 == startHttp){
//            if (list != null && !list.isEmpty()){
//                this.mList.clear();
//                this.mList.addAll(list);
//                notifyDataSetChanged();
//            }
//        }else if (startHttp > 0){
//            if (list != null && !list.isEmpty() ){
//                this.mList.addAll(list);
//                notifyItemRangeInserted(this.mList.size() - list.size() , list.size());
//            }
//        }
//
//
//    }



       class MyViewHolder extends RecyclerView.ViewHolder {

         TextView tv_title;
         CheckBox checkBox;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
