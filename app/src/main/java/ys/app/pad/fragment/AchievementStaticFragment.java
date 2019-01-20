package ys.app.pad.fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ys.app.pad.BaseFragment;
import ys.app.pad.R;
import ys.app.pad.databinding.FragmentAchievementStaticBinding;
import ys.app.pad.viewmodel.AchievementStaticFragmentViewModel;

/**
 * Created by Administrator on 2017/11/9/009.
 * 业绩统计
 */

public class AchievementStaticFragment extends BaseFragment {

    private View view;
    private FragmentAchievementStaticBinding binding;
    private AchievementStaticFragmentViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_achievement_static, container, false);
            view = binding.getRoot();
            mViewModel = new AchievementStaticFragmentViewModel(this, binding);
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

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

}
