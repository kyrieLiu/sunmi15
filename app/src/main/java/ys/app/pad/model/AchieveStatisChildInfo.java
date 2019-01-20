package ys.app.pad.model;

import java.util.Observable;

/**
 * Created by Administrator on 2017/11/15/015.
 */

public class AchieveStatisChildInfo extends Observable {

    /**
     * percentage : null
     * identification : null
     * money : 0
     * unit : null
     * name : 主粮系列
     * id : 164
     * number : null
     */

    private Object percentage;
    private Object identification;
    private double money;
    private Object unit;
    private String name;
    private int id;
    private Object number;

    public Object getPercentage() {
        return percentage;
    }

    public void setPercentage(Object percentage) {
        this.percentage = percentage;
    }

    public Object getIdentification() {
        return identification;
    }

    public void setIdentification(Object identification) {
        this.identification = identification;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Object getUnit() {
        return unit;
    }

    public void setUnit(Object unit) {
        this.unit = unit;
    }

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

    public Object getNumber() {
        return number;
    }

    public void setNumber(Object number) {
        this.number = number;
    }
}
