package ys.app.pad.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ys.app.pad.BaseFragment;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.databinding.VipConsumesBinding;
import ys.app.pad.viewmodel.vip.VipConsumesViewModel;

/**
 * Created by Administrator on 2017/6/8.
 */

public class VipConsumesFragment extends BaseFragment {

    private View view;
    private VipConsumesBinding binding;
    private VipConsumesViewModel mViewModel;
    private int mShopId;
    private long mVipUserId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vip_consume, container, false);
            view = binding.getRoot();
            mShopId = getArguments().getInt(Constants.intent_vip_shop_id);
            mVipUserId = getArguments().getLong(Constants.intent_vip_user_id);
            mViewModel = new VipConsumesViewModel(this, binding);
            binding.setViewModel(mViewModel);
            mViewModel.init(mShopId,mVipUserId);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void initData() {

    }
}
