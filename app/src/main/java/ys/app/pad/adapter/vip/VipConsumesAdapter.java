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
import ys.app.pad.databinding.ItemVipConsumeBinding;
import ys.app.pad.model.OrderInfo;
import ys.app.pad.utils.StringUtils;
import ys.app.pad.viewmodel.ItemVipConsumeViewModel;
import ys.app.pad.widget.wrapperRecylerView.IViewHolder;

/**
 * Created by Administrator on 2017/6/8.
 */

public class VipConsumesAdapter extends RecyclerView.Adapter<VipConsumesAdapter.MyViewHolder> {

    private final Context mContext;


    public List<OrderInfo> getList() {
        return mList;
    }

    private List<OrderInfo> mList;
    private OnItemClickListener listener;


    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public VipConsumesAdapter(Context context) {
        mList = new ArrayList<>();
        this.mContext = context;
    }

    @Override
    public VipConsumesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemVipConsumeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_vip_consume, parent, false);
        return new VipConsumesAdapter.MyViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(VipConsumesAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position, mContext);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<OrderInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(int startHttp, List<OrderInfo> list) {
        if (0 == startHttp) {
            if (list != null && !list.isEmpty()) {
                this.mList.clear();
                this.mList.addAll(list);
                notifyDataSetChanged();
            }
        } else if (startHttp > 0) {
            if (list != null && !list.isEmpty()) {
                this.mList.addAll(list);
                notifyItemRangeInserted(this.mList.size() - list.size(), list.size());
            }
        }


    }


    public static class MyViewHolder extends IViewHolder {

        private final OnItemClickListener mListenner;
        private ItemVipConsumeBinding mBinding;

        public MyViewHolder(@NonNull ItemVipConsumeBinding binding, OnItemClickListener listener) {
            super(binding.rootView);
            this.mBinding = binding;
            this.mListenner = listener;
        }


        public void bind(OrderInfo itemModle, final int position, Context mContext) {
            ItemVipConsumeViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new ItemVipConsumeViewModel(itemModle, mContext);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
            }

            String pay_type=itemModle.getPayedMethod();
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
            if (mBinding != null) {
                final View root = mBinding.getRoot();
                root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListenner != null) {
                            mListenner.onItemClick(root, position);
                        }
                    }
                });
            }
        }
    }
}
