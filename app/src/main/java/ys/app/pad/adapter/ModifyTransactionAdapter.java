package ys.app.pad.adapter;

import android.app.Activity;

import java.util.List;

import ys.app.pad.model.EmployeeInfo;

/**
 * 作者：lv
 * 时间：2017/3/21 22:20
 */

public class ModifyTransactionAdapter extends SelectAdapter<EmployeeInfo> {

    public ModifyTransactionAdapter(Activity activity, List<EmployeeInfo> list) {
        super(activity,list);
    }

    @Override
    public void onBindView(SelectAdapter.MyViewHolder holder, int position) {
        EmployeeInfo type = (EmployeeInfo)mList.get(position);
        holder.setName(type.getName());
        holder.setSelect(type.isSelect());
    }
}
