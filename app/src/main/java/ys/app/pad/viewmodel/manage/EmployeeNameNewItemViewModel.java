package ys.app.pad.viewmodel.manage;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import ys.app.pad.Constants;
import ys.app.pad.activity.manage.EmployeePerformanceActivity;
import ys.app.pad.model.EmployeeInfo;

/**
 * Created by aaa on 2017/5/2.
 */

public class EmployeeNameNewItemViewModel extends BaseObservable {
    private EmployeeInfo model;
    private Context mContext;


    public EmployeeNameNewItemViewModel(EmployeeInfo model, Context context)
    {
        this.model = model;
        this.mContext = context;
    }


    @Bindable
    public EmployeeInfo getModel() {
        return model;
    }

    public void setModel(EmployeeInfo model) {
        this.model = model;
    }

    public void onClickButton(View V){
        Intent intent = new Intent(mContext,EmployeePerformanceActivity.class);
        intent.putExtra(Constants.intent_id,model.getId());
        intent.putExtra(Constants.intent_name,model.getName());
        mContext.startActivity(intent);
    }
}
