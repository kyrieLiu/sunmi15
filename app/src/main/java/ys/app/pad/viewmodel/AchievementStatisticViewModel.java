package ys.app.pad.viewmodel;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.BaseActivityViewModel;
import ys.app.pad.Constants;
import ys.app.pad.R;
import ys.app.pad.activity.AchievementStatisticActivity;
import ys.app.pad.databinding.AchievementStatisticBinding;
import ys.app.pad.fragment.AchievementStaticFragment;

/**
 * Created by Administrator on 2017/11/9/009.
 */

public class AchievementStatisticViewModel extends BaseActivityViewModel {
    private AchievementStatisticActivity mActivity;
    private AchievementStatisticBinding binding;
    private TextView commodityTV, serviceTV;
    private FrameLayout frameLayout;
    private List<AchievementStaticFragment> mList;


    public AchievementStatisticViewModel(AchievementStatisticActivity mActivity, AchievementStatisticBinding binding) {
        this.mActivity = mActivity;
        this.binding = binding;
        this.commodityTV = binding.commodityTv;
        this.serviceTV = binding.serviceTv;
        this.frameLayout = binding.frameLayout;
        mList = new ArrayList<>();
        init();
    }

    private void init() {
        initFragment();
        replaceFragment(0);
        commodityTV.setSelected(true);
        serviceTV.setSelected(false);
    }

    private void initFragment() {
        AchievementStaticFragment fragment1 = new AchievementStaticFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt(Constants.TYPE, AchievementStatisticActivity.COMMODITY);
        fragment1.setArguments(bundle1);
        mList.add(fragment1);
        AchievementStaticFragment fragment2 = new AchievementStaticFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt(Constants.TYPE, AchievementStatisticActivity.SERVICE);
        fragment2.setArguments(bundle2);
        mList.add(fragment2);
    }

    private void replaceFragment(int postion) {
        FragmentTransaction transaction = mActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, mList.get(postion));
        transaction.commit();
    }

    public void clickCommodity(View view) {
        replaceFragment(0);
        commodityTV.setSelected(true);
        serviceTV.setSelected(false);
    }

    public void clickService(View view) {
        replaceFragment(1);
        commodityTV.setSelected(false);
        serviceTV.setSelected(true);
    }
}
