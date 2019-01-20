package ys.app.pad.base;

import android.content.Context;
import android.databinding.BaseObservable;


/**
 * Created by admin on 2017/7/15.
 */

public abstract class MvvmCommonItemViewModel<T> extends BaseObservable {
//    public ObservableField<Integer> obTextColor = new ObservableField<>(ColorUtils.getInstance().getTextColor());
//    public ObservableField<Integer> obTextGreyColor = new ObservableField<>(ColorUtils.getInstance().getTextGreyColor());
//    public ObservableField<Integer> obBgGreyColor = new ObservableField<>(ColorUtils.getInstance().getBgGreyColor());
//    public ObservableField<Integer> obBgContentColor = new ObservableField<>(ColorUtils.getInstance().getBgContentColor());
//    public ObservableField<Integer> obDateTextColor = new ObservableField<>(ColorUtils.getInstance().getTextGreyColor());
//    public ObservableField<Integer> obDivideColor = new ObservableField<>(ColorUtils.getInstance().getDivideLineColor());

    public abstract <T> void setModel(T itemModle);
    public void setContext(Context context){
    };
}
