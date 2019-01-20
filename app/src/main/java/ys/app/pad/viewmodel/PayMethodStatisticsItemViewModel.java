package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import ys.app.pad.model.StatisticsDataInfo;

/**
 * Created by aaa on 2017/6/8.
 */

public class PayMethodStatisticsItemViewModel extends BaseObservable {

    private StatisticsDataInfo.PayMethodListBean model;
    private Context mContext;


    public PayMethodStatisticsItemViewModel(StatisticsDataInfo.PayMethodListBean model, Context context) {
        this.model = model;
        this.mContext = context;
    }


    @Bindable
    public StatisticsDataInfo.PayMethodListBean getModel() {
        return model;
    }

    public void setModel(StatisticsDataInfo.PayMethodListBean model) {
        this.model = model;
    }
}
