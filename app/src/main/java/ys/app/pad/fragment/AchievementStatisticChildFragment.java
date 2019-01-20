package ys.app.pad.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ys.app.pad.BaseFragment;
import ys.app.pad.R;
import ys.app.pad.databinding.AchievementStatisticChildBinding;
import ys.app.pad.viewmodel.achievement.AchieveStatisChildFragmentViewModel;


/**
 * Created by Administrator on 2017/11/10/010.
 */

public class AchievementStatisticChildFragment extends BaseFragment {

    private View view;
    private AchievementStatisticChildBinding binding;
    private AchieveStatisChildFragmentViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_achievement_static_item, container, false);
            view = binding.getRoot();
            mViewModel = new AchieveStatisChildFragmentViewModel(this,binding);
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
}
