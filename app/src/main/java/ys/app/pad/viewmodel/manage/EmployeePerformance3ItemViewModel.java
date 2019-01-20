package ys.app.pad.viewmodel.manage;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.model.EmployeePerformanceInfo;

/**
 * Created by aaa on 2017/5/2.
 */
public class EmployeePerformance3ItemViewModel extends BaseObservable {
    private EmployeePerformanceInfo.RechargeListBean model;
    private Context mContext;


    public EmployeePerformance3ItemViewModel(EmployeePerformanceInfo.RechargeListBean model, Context context) {
        this.model = model;
        this.mContext = context;
    }


    @Bindable
    public EmployeePerformanceInfo.RechargeListBean getModel() {
        return model;
    }

    public void setModel(EmployeePerformanceInfo.RechargeListBean model) {
        this.model = model;
    }
}
