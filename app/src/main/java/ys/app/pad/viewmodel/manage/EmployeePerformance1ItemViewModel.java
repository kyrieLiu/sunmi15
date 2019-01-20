package ys.app.pad.viewmodel.manage;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.model.EmployeePerformanceInfo;

/**
 * Created by aaa on 2017/5/2.
 */
public class EmployeePerformance1ItemViewModel extends BaseObservable {
    private EmployeePerformanceInfo.OrderDetailedListCommodityBean model;
    private Context mContext;


    public EmployeePerformance1ItemViewModel(EmployeePerformanceInfo.OrderDetailedListCommodityBean model, Context context) {
        this.model = model;
        this.mContext = context;
    }


    @Bindable
    public EmployeePerformanceInfo.OrderDetailedListCommodityBean getModel() {
        return model;
    }

    public void setModel(EmployeePerformanceInfo.OrderDetailedListCommodityBean model) {
        this.model = model;
    }
}
