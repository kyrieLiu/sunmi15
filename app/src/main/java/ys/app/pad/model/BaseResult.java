package ys.app.pad.model;

/**
 * Created by lyy on 2017/2/28 15:35.
 * email：2898049851@qq.com
 */

public class BaseResult {


    /**
     * success : true
     * errorMessage : null
     * data : null
     * errorCode : null
     * message : null
     */

    private boolean success;
    private String errorMessage;
    private String data;
    private String errorCode;
    private String message;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
}
