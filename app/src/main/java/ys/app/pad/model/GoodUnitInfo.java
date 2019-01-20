package ys.app.pad.model;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by aaa on 2017/3/22.
 */

public class GoodUnitInfo extends BaseSelect{

    /**
     * name : 个
     * id : 3
     * type : 1
     */

    private String name;
    private int id;
    private int type;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
