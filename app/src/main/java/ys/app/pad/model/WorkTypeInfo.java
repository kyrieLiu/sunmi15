package ys.app.pad.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/7.
 */

public class WorkTypeInfo extends BaseSelect implements Serializable {


    /**
     * name : 美容师助理
     * id : 3
     */

    private String name;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
