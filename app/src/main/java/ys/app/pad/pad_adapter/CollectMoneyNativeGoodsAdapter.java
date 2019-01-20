package ys.app.pad.pad_adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.model.GoodTypeInfo;

/**
 * Created by Administrator on 2016/5/6 0006.
 */
public class CollectMoneyNativeGoodsAdapter extends RecyclerView.Adapter<CollectMoneyNativeGoodsAdapter.MyViewHolder> {

    private Context context;
    private List<GoodTypeInfo> list;
    private  OnItemClickListener listener;
    private LayoutInflater inflater;

    public CollectMoneyNativeGoodsAdapter(Context context, List<GoodTypeInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public  void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_layout_collect_money_navicat, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final GoodTypeInfo item = list.get(position);
        holder.setTitle(position,item.getName());
        holder.isSelect(item.getSelect());
        holder.rl_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        private RelativeLayout rl_bg;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tv_item_collect_money_title);
            rl_bg=(RelativeLayout)itemView.findViewById(R.id.rl_item_goods);

        }
        private void setTitle(int position,String name){
            title.setText(name);
        }

        public void isSelect(int select) {
            if (1 == select) {
                rl_bg.setBackgroundColor(context.getResources().getColor(R.color.color_main));
            } else {
                rl_bg.setBackgroundResource(0);
            }
        }
    }

}
