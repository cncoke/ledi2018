package cn.yc.ledi.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-06-26.
 */

public class GoldRecordBean implements Serializable {

    private int id;
    private String payname;
    private String order_sn;
    private int type;
    private float num;
    private long inputtime;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPayname() {
        return payname;
    }

    public void setPayname(String payname) {
        this.payname = payname;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public long getInputtime() {
        return inputtime;
    }

    public void setInputtime(long inputtime) {
        this.inputtime = inputtime;
    }

    @Override
    public String toString() {
        return "GoldRecordBean{" +
                "id=" + id +
                ", payname='" + payname + '\'' +
                ", num=" + num +
                ", inputtime=" + inputtime +
                '}';
    }
}
