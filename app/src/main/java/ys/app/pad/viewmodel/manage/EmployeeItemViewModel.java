package ys.app.pad.viewmodel.manage;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;
import ys.app.pad.model.EmployeeInfo;

/**
 * 作者：lv
 * 时间：2017/3/29 19:58
 */

public class EmployeeItemViewModel extends BaseObservable {
    private EmployeeInfo model;
    private Context mContext;


    public EmployeeItemViewModel(EmployeeInfo model, Context context)
    {
        this.model = model;
        this.mContext = context;
        setModel(model);
    }


    @Bindable
    public EmployeeInfo getModel() {
        return model;
    }

    public void setModel(EmployeeInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }
}
