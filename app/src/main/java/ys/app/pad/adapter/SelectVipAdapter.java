package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.ChargeInfo;

/**
 * Created by aaa on 2017/3/28.
 */

public class SelectVipAdapter extends  SelectAdapter{
    public SelectVipAdapter(Activity activity, List<ChargeInfo> list) {
        super(activity, list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        ChargeInfo type = (ChargeInfo) mList.get(position);
        holder.setName("卡号: "+type.getCardNo());
        holder.setSelect(type.isSelect());
    }
    
}
