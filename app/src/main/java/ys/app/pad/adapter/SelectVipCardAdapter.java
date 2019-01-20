package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.VipCardInfo;

/**
 * Created by aaa on 2017/3/24.
 */

public class SelectVipCardAdapter extends SelectAdapter{

    public SelectVipCardAdapter(Activity activity, List<VipCardInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        VipCardInfo type = (VipCardInfo) mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
