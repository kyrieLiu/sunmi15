package ys.app.pad.model;

/**
 * Created by liuyin on 2017/9/12.
 */

public class AppointmentMainBean {

    /**
     * bespeakDay : 10月13日Friday
     * checkNumber : 0
     * failNumber : 0
     * successNumber : 2
     */

    private String bespeakDay;
    private int checkNumber;
    private int failNumber;
    private int successNumber;

    public String getBespeakDay() {
        return bespeakDay;
    }

    public void setBespeakDay(String bespeakDay) {
        this.bespeakDay = bespeakDay;
    }

    public int getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(int checkNumber) {
        this.checkNumber = checkNumber;
    }

    public int getFailNumber() {
        return failNumber;
    }

    public void setFailNumber(int failNumber) {
        this.failNumber = failNumber;
    }

    public int getSuccessNumber() {
        return successNumber;
    }

    public void setSuccessNumber(int successNumber) {
        this.successNumber = successNumber;
    }
}
