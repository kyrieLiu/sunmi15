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
import ys.app.pad.databinding.FragmentVipCardBinding;
import ys.app.pad.viewmodel.vip.VipCardFragmentViewModel;


public class VipCardFragment extends BaseFragment {

    private View view;
    private FragmentVipCardBinding binding;
    private VipCardFragmentViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_vip_card, container, false);
            view = binding.getRoot();
            Bundle bundle = getArguments();
            int  classification = bundle.getInt(Constants.intent_type);
            int cardNO=bundle.getInt(Constants.intent_vip_card_no);
            int flag=bundle.getInt(Constants.intent_flag);
            mViewModel = new VipCardFragmentViewModel(this,binding,flag,classification);
            mViewModel.setIntentCardNO(cardNO);
            binding.setViewModel(mViewModel);
        }

        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    @Override
    public void initData() {
        mViewModel.init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.clear();
    }
}
