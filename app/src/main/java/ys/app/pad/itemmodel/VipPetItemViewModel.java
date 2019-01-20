package ys.app.pad.itemmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.BaseActivity;
import ys.app.pad.Constants;
import ys.app.pad.activity.vip.VipDetailActivity;
import ys.app.pad.model.VipInfo;
import ys.app.pad.utils.StringUtils;

/**
 * Created by aaa on 2017/3/17.
 */

public class VipPetItemViewModel extends BaseObservable {

    private VipInfo model;
    private BaseActivity activity;
    public ObservableField<String> vipName=new ObservableField<>();
    public ObservableField<String> vipPhone=new ObservableField<>();


    public VipPetItemViewModel(int position, VipInfo model, BaseActivity activity)
    {
        this.model = model;
        this.activity=activity;
        setModel(model);
    }



    @Bindable
    public VipInfo getModel() {
        return model;
    }

    public void setModel(VipInfo model) {
        this.model = model;
        vipName.set("会员: "+model.getName());
        vipPhone.set("电话: "+model.getPhone());
        notifyPropertyChanged(BR.model);
    }
    public void clickDetail(){
        Intent intent = new Intent(activity,VipDetailActivity.class);
        intent.putExtra(Constants.intent_info,model);
        if (StringUtils.isEmpty(model.getProductList())){
            intent.putExtra(Constants.intent_type,0);
        }else{
            intent.putExtra(Constants.intent_type,1);
        }

        activity.startActivity(intent);
    }


}
