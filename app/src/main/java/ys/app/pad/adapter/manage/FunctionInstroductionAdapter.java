package ys.app.pad.adapter.manage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.manage.FunctionUpdateDetailActivity;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.FunctionIntroductionBean;

/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class FunctionInstroductionAdapter extends RecyclerView.Adapter<FunctionInstroductionAdapter.MyViewHolder> {

    private final Context mContext;

    public List<FunctionIntroductionBean> getList() {
        return mList;
    }

    private List<FunctionIntroductionBean> mList;


    private OnItemClickListener listener;

    private LayoutInflater mInflater;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public FunctionInstroductionAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        initData();
    }
    private void initData(){
        FunctionIntroductionBean bean=new FunctionIntroductionBean();
        bean.setTitle("智选E派1.1.9主要更新");
        bean.setTime("2018年6月20号");
        bean.setInstroduction("本次更新\n" +
                "1. 【功能介绍】新增功能介绍, 提示并解答版本主要更新内容, 操作更易上手\n" +
                "2. 【收银小票】增加收银小票是否展现会员价权限设置, 默认不展示, 如需小票展示会员价, 则请前往管理-系统设置-收银小票设置进行设置选择开通.");
        mList.add(bean);
        FunctionIntroductionBean bean14=new FunctionIntroductionBean();
        bean14.setTitle("智选E派1.1.4主要更新");
        bean14.setTime("2018年6月14号");
        bean14.setInstroduction("本次更新\n" +
                "1. 【新增】宠物店主心心念念的会员价已上线~\n" +
                "2. 【优化】商品库存增加扫描搜索\n" +
                "3. 【修复】修复已知问题, 性能提升\n" +
                "4. 【界面】优化部分界面设计，提升视觉效果\n" +
                "5. 【体验】调整部分产品模块，提升部分功能体验");
        mList.add(bean14);

    }
    @Override
    public FunctionInstroductionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_function_introduction, parent, false);
        MyViewHolder viewHolder=new FunctionInstroductionAdapter.MyViewHolder(view);
        viewHolder.tv_title= (TextView) view.findViewById(R.id.tvitem_function_title);
        viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_item_update_time);
        viewHolder.rl_parent= (RelativeLayout) view.findViewById(R.id.rl_item_update_function_parent);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FunctionInstroductionAdapter.MyViewHolder holder, final int position) {
        FunctionIntroductionBean info=mList.get(position);
        holder.tv_title.setText(info.getTitle());
        holder.tv_time.setText(info.getTime());
        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, FunctionUpdateDetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(Constants.intent_info,mList.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void removeData(int registerI) {
        mList.remove(registerI);
        notifyItemRemoved(registerI);
        if (registerI != mList.size()) {
            notifyItemRangeChanged(registerI, mList.size() - registerI);
        }
    }

    public void setList(List<FunctionIntroductionBean> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_time;
        private RelativeLayout rl_parent;
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
