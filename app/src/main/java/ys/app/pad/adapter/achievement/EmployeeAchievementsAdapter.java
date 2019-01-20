package ys.app.pad.adapter.achievement;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.EmployeeAchievementsInfo;
import ys.app.pad.utils.MathUtil;


/**
 * Created by aaa on 2017/3/20.
 */

public  class EmployeeAchievementsAdapter extends RecyclerView.Adapter<EmployeeAchievementsAdapter.MyViewHolder> {

    private final Activity mActivity;
    protected List<EmployeeAchievementsInfo> mList = new ArrayList<>();
    private int lastSelectPos = -1;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;


    public EmployeeAchievementsAdapter(Activity activity) {
        this.mActivity = activity;
        this.mList.clear();
    }


    @Override
    public void onBindViewHolder(EmployeeAchievementsAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position);
    }


    @Override
    public EmployeeAchievementsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mActivity)
                .inflate(R.layout.item_employee_achievements, parent, false);
        return new EmployeeAchievementsAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public void setList(int startHttp, List<EmployeeAchievementsInfo> list) {
        if( 0 == startHttp){
            mList.clear();
        }

        int positionStart = mList.size();
        int itemCount = list.size();
        mList.addAll(list);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }

    }
    public EmployeeAchievementsInfo getObject(){
        return  mList.get(lastSelectPos);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mTvName,mTvCommodity,mTvService,mTvMoney;
        ImageView checkIv;
        private View itemView;


        public MyViewHolder(final View itemView) {
            super(itemView);
            this.itemView=itemView;

            mTvName = (TextView) itemView.findViewById(R.id.tv_item_achievement_name);
            mTvCommodity = (TextView) itemView.findViewById(R.id.tv_item_achievement_commodity);
            mTvService = (TextView) itemView.findViewById(R.id.tv_item_achievement_service);
            mTvMoney = (TextView) itemView.findViewById(R.id.tv_item_achievement_money);


        }
        public void bind(EmployeeAchievementsInfo model, final int position){
            mTvName.setText(model.getName());
            mTvCommodity.setText("¥"+MathUtil.formatStandardMoney(model.getShopTotalSales()));
            mTvService.setText("¥"+MathUtil.formatStandardMoney(model.getProductTotalSales()));
            mTvMoney.setText("¥"+MathUtil.formatStandardMoney(model.getTotalSales()));
        }

    }
}
