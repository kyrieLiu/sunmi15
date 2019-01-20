package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.GoodUnitInfo;

/**
 * Created by aaa on 2017/3/22.
 */

public class SelectUnitAdapter  extends SelectAdapter<GoodUnitInfo> {

    public SelectUnitAdapter(Activity activity, List<GoodUnitInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        GoodUnitInfo type = (GoodUnitInfo) mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
