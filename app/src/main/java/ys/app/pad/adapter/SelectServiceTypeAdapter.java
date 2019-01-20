package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.ServiceTypeInfo;

/**
 * 作者：lv
 * 时间：2017/4/3 17:37
 */

public class SelectServiceTypeAdapter extends SelectAdapter{
    public SelectServiceTypeAdapter(Activity activity, List<ServiceTypeInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        ServiceTypeInfo type = (ServiceTypeInfo) mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
