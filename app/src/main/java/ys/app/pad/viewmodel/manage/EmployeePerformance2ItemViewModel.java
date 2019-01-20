package ys.app.pad.viewmodel.manage;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.model.EmployeePerformanceInfo;

/**
 * Created by aaa on 2017/5/2.
 */
public class EmployeePerformance2ItemViewModel extends BaseObservable {
    private EmployeePerformanceInfo.OrderDetailedListProductBean model;
    private Context mContext;


    public EmployeePerformance2ItemViewModel(EmployeePerformanceInfo.OrderDetailedListProductBean model, Context context) {
        this.model = model;
        this.mContext = context;
    }


    @Bindable
    public EmployeePerformanceInfo.OrderDetailedListProductBean getModel() {
        return model;
    }

    public void setModel(EmployeePerformanceInfo.OrderDetailedListProductBean model) {
        this.model = model;
    }
}