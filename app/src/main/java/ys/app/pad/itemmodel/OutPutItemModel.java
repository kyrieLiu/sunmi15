package ys.app.pad.itemmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.Constants;
import ys.app.pad.activity.inventory.OutStorageDetailActivity;
import ys.app.pad.fragment.OutPutInventoryFragment;
import ys.app.pad.model.OutStorageInfo;


/**
 * Created by liuyin on 2017/9/13.
 */

public class OutPutItemModel extends BaseObservable {
    private OutStorageInfo model;
    private OutPutInventoryFragment fragment;
    private int type;

    public OutPutItemModel(OutStorageInfo model, OutPutInventoryFragment fragment, int type) {
        this.model=model;
        this.fragment=fragment;
        this.type=type;
    }

    @Bindable
    public OutStorageInfo getModel() {
        return model;
    }

    public void setModel(OutStorageInfo model) {
        this.model = model;
        notifyPropertyChanged(BR.model);
    }

    public void clickItem(View view){
        Intent intent = new Intent(fragment.getActivity(),OutStorageDetailActivity.class);
        intent.putExtra(Constants.intent_flag,type);
        intent.putExtra(Constants.intent_info,model);
        fragment.startActivity(intent);
    }


}
