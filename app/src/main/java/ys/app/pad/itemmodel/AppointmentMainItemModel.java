package ys.app.pad.itemmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.model.AppointmentMainBean;


/**
 * Created by liuyin on 2017/9/12.
 */

public class AppointmentMainItemModel extends BaseObservable {
    private AppointmentMainBean model;
    public ObservableField<String> day=new ObservableField<>();
    public ObservableField<String> week=new ObservableField<>();


    public AppointmentMainItemModel(AppointmentMainBean model) {
        splitDate(model.getBespeakDay());
        this.model = model;
    }

    @Bindable
    public AppointmentMainBean getModel() {
        return model;
    }

    public void setModel(AppointmentMainBean model) {
        this.model = model;
        splitDate(model.getBespeakDay());
        notifyPropertyChanged(BR.model);
    }
    private void splitDate(String bespeakDay){
        String dayTime=bespeakDay.substring(0,6);
        String weekTime=bespeakDay.substring(6,bespeakDay.length());
        String turnWeek="";
        switch (weekTime){
            case "Monday":
                turnWeek="星期一";
                break;
            case "Tuesday":
                turnWeek="星期二";
                break;
            case "Wednesday":
                turnWeek="星期三";
                break;
            case "Thursday":
                turnWeek="星期四";
                break;
            case "Friday":
                turnWeek="星期五";
                break;
            case "Saturday":
                turnWeek="星期六";
                break;
            case "Sunday":
                turnWeek="星期日";
                break;
        }
        day.set(dayTime);
        week.set(turnWeek);
    }
}
