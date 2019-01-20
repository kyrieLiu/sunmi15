package ys.app.pad.adapter.vip;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;

/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class VipDetailMenuAdapter extends RecyclerView.Adapter<VipDetailMenuAdapter.MyViewHolder> {

    private  Context mContext;


    private OnItemClickListener listener;



    private LayoutInflater mInflater;
    private int[]images=new int[]{R.mipmap.icon_record,R.mipmap.icon_pet,R.mipmap.icon_modify,R.mipmap.icon_money,R.mipmap.icon_refund_card};
    private String[]menus=new String[]{"充值消费","新增宠物","修改会员","会员充值","会员退卡"};

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VipDetailMenuAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public VipDetailMenuAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_vip_detail_menu, parent, false);
        MyViewHolder viewHolder=new VipDetailMenuAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VipDetailMenuAdapter.MyViewHolder holder, int position) {
                holder.bind(listener,images[position],menus[position],position);
    }


    @Override
    public int getItemCount() {
        return images.length;
    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_menu;
        private ImageView iv_icon;
        private RelativeLayout rl_root;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_menu= (TextView) itemView.findViewById(R.id.iv_item_vip_detail_menu);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_item_vip_detail_icon);
            rl_root= (RelativeLayout) itemView.findViewById(R.id.root_view);
        }
        private void bind(final OnItemClickListener listener, int image, String menu, final int position){
            tv_menu.setText(menu);
            iv_icon.setImageResource(image);
            rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   if (listener!=null){
                       listener.onItemClick(itemView,position);
                   }
                }
            });
        }
    }

}
