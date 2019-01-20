package ys.app.pad.model;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by admin on 2017/6/10.
 */

public class DiscountInfo extends BaseSelect {
    /**
     * name : 个
     * id : 3
     * type : 1
     */

    private String name;
    private int type;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }
}
