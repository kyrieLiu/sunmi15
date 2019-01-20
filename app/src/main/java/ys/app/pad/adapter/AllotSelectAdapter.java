package ys.app.pad.adapter;

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
import ys.app.pad.model.AllotSelectModel;


/**
 * Created by aaa on 2017/3/20.
 */

public  class AllotSelectAdapter extends RecyclerView.Adapter<AllotSelectAdapter.MyViewHolder> {

    private final Activity mActivity;
    protected List<AllotSelectModel> mList = new ArrayList<>();
    private int lastSelectPos = -1;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;


    public AllotSelectAdapter(Activity activity) {
        this.mActivity = activity;
        this.mList.clear();
    }


    @Override
    public void onBindViewHolder(AllotSelectAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position);
    }


    @Override
    public AllotSelectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mActivity)
                .inflate(R.layout.item_allot_select, parent, false);
        return new AllotSelectAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }



    public void setList(int startHttp, List<AllotSelectModel> list) {
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
    public AllotSelectModel getObject(){
        return  mList.get(lastSelectPos);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        ImageView checkIv;
        private View itemView;


        public MyViewHolder(final View itemView) {
            super(itemView);
            this.itemView=itemView;

            nameTv = (TextView) itemView.findViewById(R.id.name_tv);
            checkIv = (ImageView) itemView.findViewById(R.id.check_iv);


        }
        public void bind(AllotSelectModel model, final int position){
            checkIv.setSelected(model.isSelect());
            nameTv.setText(model.getShopName());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectPos = position;
                    if (-1 != lastSelectPos) {
                        mList.get(lastSelectPos).setSelect(false);
                    }
                    mList.get(selectPos).setSelect(true);
                    lastSelectPos = selectPos;
                    if (listener != null) {
                        listener.onItemClick(itemView, lastSelectPos);
                    }
                    notifyDataSetChanged();
                }
            });
        }

    }
}
