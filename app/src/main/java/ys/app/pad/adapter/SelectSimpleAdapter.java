package ys.app.pad.adapter;

import android.app.Activity;
import android.content.Context;

import java.util.List;

import ys.app.pad.model.SelectInfo;

/**
 * Created by aaa on 2017/3/20.
 */

public class SelectSimpleAdapter extends SelectAdapter<SelectInfo> {

    public SelectSimpleAdapter(Context activity, List<SelectInfo> list) {
        super(activity,list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        SelectInfo info = (SelectInfo)mList.get(position);
        holder.setName(info.getName());
        holder.setSelect(info.isSelect());
    }
}
