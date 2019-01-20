package ys.app.pad.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.R;
import ys.app.pad.activity.vip.VipDetailActivity;
import ys.app.pad.callback.OnItemClickListener;
import ys.app.pad.databinding.ItemAnimalBinding;
import ys.app.pad.model.AnimalInfo;
import ys.app.pad.viewmodel.AnimalItemViewModel;

/**
 * Created by aaa on 2017/3/17.
 */

public class AnimalAdapter extends RecyclerView.Adapter<AnimalAdapter.MyViewHolder> {

    private final VipDetailActivity mActivity;
    private List<AnimalInfo> mList;

    public static void setListener(OnItemClickListener listener) {
        AnimalAdapter.listener = listener;
    }

    private static OnItemClickListener listener;

    public AnimalAdapter(VipDetailActivity mActivity)
    {
        mList = new ArrayList<>();
        this.mActivity = mActivity;
    }

    @Override
    public AnimalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemAnimalBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_animal, parent, false);
        return new AnimalAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AnimalAdapter.MyViewHolder holder, int position) {
        holder.bind(mList.get(position), position,mActivity);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setList(List<AnimalInfo> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }



    public static class MyViewHolder  extends RecyclerView.ViewHolder {

        private ItemAnimalBinding mBinding;

        public MyViewHolder(@NonNull ItemAnimalBinding binding) {
            super(binding.rootView);
            this.mBinding = binding;
        }

        public void bind(AnimalInfo itemModle, final int position,VipDetailActivity mActivity) {
            AnimalItemViewModel itemViewModel;
            if (mBinding.getItemViewModel() == null) {
                itemViewModel = new AnimalItemViewModel(itemModle, mActivity);
                mBinding.setItemViewModel(itemViewModel);
            } else {
                itemViewModel = mBinding.getItemViewModel();
                itemViewModel.setModel(itemModle);
                mBinding.setItemViewModel(itemViewModel);
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
