package ys.app.pad.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.android.databinding.library.baseAdapters.BR;

import ys.app.pad.model.ServiceTypeInfo;
import ys.app.pad.utils.AppUtil;
import ys.app.pad.utils.Logger;

/**
 * Created by admin on 2017/7/5.
 */

public class ServiceTypeInVipItemViewModel extends BaseObservable {
    private ServiceTypeInfo model;
    private Context mContext;
    public ObservableBoolean mIsInput = new ObservableBoolean();
    public ObservableBoolean mIsAddVipCard = new ObservableBoolean();
    public ObservableField<String> obGoodCost = new ObservableField<>();
    public ObservableField<String> obServiceCost = new ObservableField<>();
    public ObservableBoolean editTextEditable = new ObservableBoolean();


    public ServiceTypeInVipItemViewModel(ServiceTypeInfo model, Context context,boolean isInput,boolean isAddVipCard)
    {
        mIsInput.set(isInput);
        mIsAddVipCard.set(isAddVipCard);
        if (isAddVipCard){
            model.setDiscount(1);
        }
        if (!isInput && !isAddVipCard){
            editTextEditable.set(false);
        }else {
            editTextEditable.set(true);
        }
        this.model = model;
        this.mContext = context;
        obServiceCost.set(AppUtil.noDiscountShow(model.getDiscount()));
    }



    @Bindable
    public ServiceTypeInfo getModel() {
        return model;
    }

    public void setModel(ServiceTypeInfo model,boolean isInput,boolean isAddVipCard) {
        mIsInput.set(isInput);
        mIsAddVipCard.set(isAddVipCard);
        if (isAddVipCard){
            model.setDiscount(1);
        }
        if (!isInput && !isAddVipCard){
            editTextEditable.set(false);
        }else {
            editTextEditable.set(true);
        }
        this.model = model;
        notifyPropertyChanged(BR.model);
        obServiceCost.set(AppUtil.noDiscountShow(model.getDiscount()));
    }

    public TextWatcher textChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String str = AppUtil.uploadDiscountStr(obServiceCost.get());
            Logger.e(" str = "+str);
            if (!TextUtils.isEmpty(str)) {
                try{
                    model.setDiscount(Double.valueOf(str));
                    Logger.e(" Double.valueOf(str) = "+Double.valueOf(str));
                }catch (NumberFormatException e){
                    Logger.e(e.getMessage());
                }
            }
        }
    };
}