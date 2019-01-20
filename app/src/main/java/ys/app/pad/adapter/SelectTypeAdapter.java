package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.GoodTypeInfo;

/**
 * 作者：lv
 * 时间：2017/3/21 22:20
 */

public class SelectTypeAdapter extends SelectAdapter<GoodTypeInfo> {

    public SelectTypeAdapter(Activity activity, List<GoodTypeInfo> list) {
        super(activity,list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        GoodTypeInfo type = (GoodTypeInfo)mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
