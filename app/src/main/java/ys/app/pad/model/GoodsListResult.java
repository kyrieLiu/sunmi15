package ys.app.pad.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;

/**
 * Created by lyy on 2017/2/9 10:45.
 * email：2898049851@qq.com
 */

public class GoodsListResult extends BaseObservable implements Serializable{
    private int id;
    private String name;
    private int price;
    private int inventoryNum;

    public GoodsListResult(int id, String name, int price, int inventoryNum) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inventoryNum = inventoryNum;
    }

    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public int getInventoryNum() {
        return inventoryNum;
    }

    public void setInventoryNum(int inventoryNum) {
        this.inventoryNum = inventoryNum;
        notifyPropertyChanged(BR.inventoryNum);
    }
}
