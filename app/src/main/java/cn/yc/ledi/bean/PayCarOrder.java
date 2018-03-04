package cn.yc.ledi.bean;

import com.mob.paysdk.PayOrder;

/**
 * Created by Administrator on 2018-03-04.
 */

public class PayCarOrder extends PayOrder {


    private String car_id;
    private int type;

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
