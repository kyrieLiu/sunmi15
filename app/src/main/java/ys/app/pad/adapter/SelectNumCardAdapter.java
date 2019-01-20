package ys.app.pad.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;

import java.util.List;

import ys.app.pad.model.NumCardListInfo;
import ys.app.pad.model.VipCardInfo;

/**
 * Created by Administrator on 2017/12/13/013.
 */

public class SelectNumCardAdapter extends SelectAdapter {
    public SelectNumCardAdapter(Activity activity, List<NumCardListInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(MyViewHolder holder, int position) {
        NumCardListInfo type = (NumCardListInfo) mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }

}
