package ys.app.pad.adapter;

import android.content.Context;

import ys.app.pad.BR;
import ys.app.pad.R;
import ys.app.pad.base.MvvmCommonAdapter;
import ys.app.pad.model.AchieveStatisChildInfo;
import ys.app.pad.viewmodel.AchievementStatisItemViewModel;


/**
 * Created by Administrator on 2017/11/10/010.
 */

public class AchieveStatisFragmentChildAdapter extends MvvmCommonAdapter<AchieveStatisChildInfo> {
    public AchieveStatisFragmentChildAdapter(Context context) {
        super(BR.itemViewModel, context, R.layout.fragment_achieve_statis_child, AchievementStatisItemViewModel.class);
    }

    public AchieveStatisFragmentChildAdapter(Context context, boolean isCanLoadMore) {
        super(BR.itemViewModel, context, R.layout.fragment_achieve_statis_child, AchievementStatisItemViewModel.class, isCanLoadMore);
    }
}
