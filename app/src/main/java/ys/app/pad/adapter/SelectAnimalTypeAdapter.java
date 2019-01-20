package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.GoodTypeInfo;

/**
 * 作者：lv
 * 时间：2017/4/3 17:37
 */

public class SelectAnimalTypeAdapter  extends SelectAdapter{
    public SelectAnimalTypeAdapter(Activity activity, List<GoodTypeInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        GoodTypeInfo type = (GoodTypeInfo) mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
