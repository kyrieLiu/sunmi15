package ys.app.pad.viewmodel.manage;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.List;

import ys.app.pad.adapter.SelectSimpleAdapter;
import ys.app.pad.adapter.SelectWorkTypeAdapter;
import ys.app.pad.model.AddEmployeeInfo;
import ys.app.pad.model.BaseListResult;
import ys.app.pad.model.SelectInfo;
import ys.app.pad.model.WorkTypeInfo;
import ys.app.pad.widget.dialog.SelectDialog;

/**
 * Created by aaa on 2017/6/6.
 */

public class AddEmployeeItemViewModel extends BaseObservable {

    private AddEmployeeInfo model;
    private Context mContext;
    private BaseListResult<WorkTypeInfo> mData;
    public ObservableField<String> workType = new ObservableField<String>();
    public ObservableField<Integer> workTypeId = new ObservableField<Integer>();
    private SelectDialog mWorkTypeSelectDialog;
    private List<WorkTypeInfo> mDataList = new ArrayList<>();
    private SelectDialog mSelectDialog;
    public ObservableField<String> gender = new ObservableField<>();



    public AddEmployeeItemViewModel(AddEmployeeInfo model, Context context, BaseListResult<WorkTypeInfo> data) {
        this.model = model;
        this.mContext = context;
        mData = data;
    }


    @Bindable
    public AddEmployeeInfo getModel() {
        return model;
    }

    public void setModel(AddEmployeeInfo model,BaseListResult<WorkTypeInfo> data) {
        this.model = model;
        mData = data;
        notifyPropertyChanged(BR.model);
    }

    public void selectTypeWork(){
        if (mData == null)return;
        mDataList = mData.getData();
        if(mDataList.isEmpty())return;
        if (mWorkTypeSelectDialog == null) {
            SelectWorkTypeAdapter adapter = new SelectWorkTypeAdapter((Activity) mContext, mDataList);
            mWorkTypeSelectDialog = new SelectDialog((Activity) mContext, adapter);
            mWorkTypeSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    WorkTypeInfo genderInfo = mDataList.get(position);
                    workTypeId.set(genderInfo.getId());
                    workType.set(genderInfo.getName());
                    model.setWorkType(genderInfo.getName());
                }
            });
        }
        mWorkTypeSelectDialog.show();
    }

    public void clickGenderButton(View v){
        if (mSelectDialog == null) {
            final List<SelectInfo> genderInfos = new ArrayList<>();
            genderInfos.add(new SelectInfo("男", "2"));
            genderInfos.add(new SelectInfo("女", "1"));
            SelectSimpleAdapter adapter = new SelectSimpleAdapter((Activity) mContext, genderInfos);
            mSelectDialog = new SelectDialog((Activity) mContext, adapter);
            mSelectDialog.setListenner(new SelectDialog.SelectListenner() {
                @Override
                public void onSelect(int position) {
                    SelectInfo genderInfo = genderInfos.get(position);
                    model.setGender(Integer.parseInt(genderInfo.getType()));
                    gender.set(genderInfo.getName());
                }
            });
        }
        mSelectDialog.show();
    }

}
