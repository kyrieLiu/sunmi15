package ys.app.pad.pad_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.model.GoodInfo;

/**
 * Created by liuyin on 2017/10/24.
 */

public class GoodsLoadAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<GoodInfo> mList = new ArrayList<>();
    private Context context;
    public GoodsLoadAdapter(Context context){
        inflater=LayoutInflater.from(context);
        this.context=context;
    }
    public void setList(List<GoodInfo> mList){
        this.mList=mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodInfo info=mList.get(position);
        MyViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new MyViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.item_goods,null,false);
            viewHolder.jine= (TextView) convertView.findViewById(R.id.jine);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (MyViewHolder) convertView.getTag();
        }
        viewHolder.jine.setText(info.getRealAmt()+"");
        return convertView;
    }
    private class MyViewHolder{
        private TextView jine;
    }
}
