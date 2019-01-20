package ys.app.pad.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.base.MvvmCommonItemViewModel;
import ys.app.pad.model.AchieveStatisChildInfo;

/**
 * Created by Administrator on 2017/11/10/010.
 */

public class AchievementStatisItemViewModel<T> extends MvvmCommonItemViewModel<T>{

    public ObservableField<Boolean> obEnd = new ObservableField<>(false);

    public AchievementStatisItemViewModel() {}

    @Bindable
    private AchieveStatisChildInfo model;

    @Bindable
    public AchieveStatisChildInfo getModel(){
        return model;
    }

    @Override
    public <T> void setModel(T model) {
        this.model = (AchieveStatisChildInfo) model;
        if (((AchieveStatisChildInfo) model).getName().equals("总计"))obEnd.set(true);
        notifyPropertyChanged(BR.model);
    }
}
