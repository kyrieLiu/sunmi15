package ys.app.pad.adapter.vip;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemVipRechargeBinding;
import ys.app.pad.model.VipRechargeInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.viewmodel.ItemVipRechargeViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by lyy on 2017/2/21 16:03.
 * email：2898049851@qq.com
 */

public class VipRechargesAdapter extends RecyclerView.Adapter<VipRechargesAdapter.MyViewHolder> {

    private final Context mContext;

    public static List<VipRechargeInfo> getList() {
        return mList;
    }

    private static List<VipRechargeInfo> mList;

    public static void setListener(OnItemClickListener listener) {
        VipRechargesAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public VipRechargesAdapter(Context context)
    {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public VipRechargesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVipRechargeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_vip_recharge, parent, false);
        return new VipRechargesAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(VipRechargesAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<VipRechargeInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(int startHttp, List<VipRechargeInfo> list) {
        if( 0 == startHttp){
            if (list != null && !list.isEmpty()){
                this.mList.clear();
                this.mList.addAll(list);
                notifyDataSetChanged();
            }
        }else if (startHttp > 0){
            if (list != null && !list.isEmpty() ){
                this.mList.addAll(list);
                notifyItemRangeInserted(this.mList.size() - list.size() , list.size());
            }
        }


    }



    public static class MyViewHolder extends IViewHolder {

        private ItemVipRechargeBinding mBinding;

        public MyViewHolder(@NonNull ItemVipRechargeBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(VipRechargeInfo itemModle, final int position, Context mContext) {
            ItemVipRechargeViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ItemVipRechargeViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }
            String pay_type=itemModle.getPayWay();
            if (StringUtils.isEmptyOrWhitespace(pay_type)) {
                mBinding.ivItemVipType.setImageResource(R.mipmap.huiyuanka);
            } else if ("1001".equals(pay_type)) {
                mBinding.ivItemVipType.setImageResource(R.mipmap.xianjin);
            } else if ("0012".equals(pay_type)||"0112".equals(pay_type)) {
                mBinding.ivItemVipType.setImageResource(R.mipmap.weixinzhifu);
            } else if ("0013".equals(pay_type)||"0113".equals(pay_type)) {
                mBinding.ivItemVipType.setImageResource(R.mipmap.zhifubao);
            } else if ("8888".equals(pay_type)) {
                mBinding.ivItemVipType.setImageResource(R.mipmap.yinhangka);
            } else {
                mBinding.ivItemVipType.setImageResource(R.mipmap.huiyuanka);
            }


            if(mBinding!=null){
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(listener != null){
                            listener.onItemClick(root, position);
                        }
                    }
                });
            }
        }
    }
}
