package ys.app.pad.model;

/**
 * Created by aaa on 2017/3/20.
 */

public class SelectInfo extends BaseSelect{
    private String name;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SelectInfo(String name, String type) {
        this.name = name;
        this.type = type;
    }
}
