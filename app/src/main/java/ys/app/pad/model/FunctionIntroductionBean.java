package ys.app.pad.model;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by liuyin on 2018/6/19.
 */

public class FunctionIntroductionBean implements Serializable{
    private String title;
    private String time;
    private String instroduction;
    private Bitmap pic;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public String getInstroduction() {
        return instroduction;
    }

    public void setInstroduction(String instroduction) {
        this.instroduction = instroduction;
    }
}
