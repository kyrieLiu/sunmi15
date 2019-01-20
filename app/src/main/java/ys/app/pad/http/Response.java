package ys.app.pad.http;


/**
 * <p>the parent class of  all the response-classes, cannot be initialized.</p>
 * Created by dennis on 2016/12/16 17:07
 * for ebrun
 * you can contact me at : pengjianjun@ebrun.com
 * or see link for more details
 *
 * @version 1.0.0
 */
public abstract class Response {

    private int code;

    private String msg;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
