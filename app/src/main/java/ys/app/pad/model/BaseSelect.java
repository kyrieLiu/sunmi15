package ys.app.pad.model;

import android.databinding.BaseObservable;

/**
 * Created by aaa on 2017/3/20.
 */

public class BaseSelect extends BaseObservable{
    boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
