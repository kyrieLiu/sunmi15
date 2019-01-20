package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.DiscountInfo;

/**
 * 作者：lv
 * 时间：2017/4/3 17:37
 */

public class SelectDiscountTypeAdapter extends SelectAdapter{
    public SelectDiscountTypeAdapter(Activity activity, List<DiscountInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        DiscountInfo type = (DiscountInfo) mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
