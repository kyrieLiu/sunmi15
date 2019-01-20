package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.Constants;
import ys.app.pad.event.RxManager;
import ys.app.pad.model.ServiceInfo;
import ys.app.pad.utils.AppUtil;

/**
 * Created by Administrator on 2017/6/10.
 */
public class ServiceSearchItemViewModel  extends BaseObservable {


    private final RxManager rxManager;
    private ServiceInfo model;
    private Context mContext;
    public ObservableField<String> price = new ObservableField<>();
    public ObservableField<String> costPrice = new ObservableField<>();

    public ServiceSearchItemViewModel(ServiceInfo model, Context context) {
        this.model = model;
        this.mContext = context;
        setPrice(model);
        rxManager = new RxManager();
    }


    @Bindable
    public ServiceInfo getModel() {
        return model;
    }

    public void setModel(ServiceInfo model) {
        this.model = model;
        setPrice(model);
        notifyPropertyChanged(BR.model);
    }

    private void setPrice(ServiceInfo model) {
        price.set(AppUtil.formatStandardMoney(model.getRealAmt()));
        costPrice.set(AppUtil.formatStandardMoney(model.getCostAmt()));
    }

    public void clickAddShoppingButton(View view){
        rxManager.post(Constants.bus_type_info,model);
    }
}
