package ys.app.pad.model;

import java.util.List;

/**
 * Created by aaa on 2017/3/20.
 */
public class BaseListResult <T extends Object> {

    /**
     * success : true
     * errorMessage : null
     * data : [{"shopId":1,"imgurl":null,"allcount":null,"count":0,"name":"狗类","id":1},{"shopId":1,"imgurl":null,"allcount":null,"count":0,"name":"猫类","id":2},{"shopId":1,"imgurl":null,"allcount":null,"count":0,"name":"鼠类","id":3}]
     * errorCode : null
     * message : null
     */

    private boolean success;
    private String errorMessage;
    private String errorCode;
    private String message;
    private List<T> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

}
