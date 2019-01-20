package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.WorkTypeInfo;

/**
 * 作者：lv
 * 时间：2017/3/21 22:20
 */

public class SelectWorkTypeAdapter extends SelectAdapter<WorkTypeInfo> {

    public SelectWorkTypeAdapter(Activity activity, List<WorkTypeInfo> list) {
        super(activity,list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        WorkTypeInfo type = (WorkTypeInfo)mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
