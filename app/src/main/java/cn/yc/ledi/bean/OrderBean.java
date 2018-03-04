package cn.yc.ledi.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by Administrator on 2017-06-15.
 */
@Table(name = "order")
public class OrderBean implements Serializable {

    @Column(name = "id", isId = true, autoGen = true)
    private int id;
    @Column(name = "order_id")
    private int order_id;
    @Column(name = "username")
    private String username;
    @Column(name = "userphone")
    private String userphone;
    @Column(name = "startpoiname")
    private String startpoiname;
    @Column(name = "startlongitude")
    private String startlongitude;
    @Column(name = "startlatitude")
    private String startlatitude;
    @Column(name = "startlocation")
    private String startlocation;
    @Column(name = "endpoiname")
    private String endpoiname;
    @Column(name = "endlongitude")
    private String endlongitude;
    @Column(name = "endlatitude")
    private String endlatitude;
    @Column(name = "endlocation")
    private String endlocation;
    @Column(name = "cartime")
    private String cartime;
    @Column(name = "num")
    private String num;
    @Column(name = "province")
    private String province;
    @Column(name = "city")
    private String city;
    @Column(name = "district")
    private String district;
    @Column(name = "car_id")
    private String car_id;
    @Column(name = "distance")
    private String distance;
    @Column(name = "state")
    private String state;
    @Column(name = "inputtime")
    private long inputtime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getStartpoiname() {
        return startpoiname;
    }

    public void setStartpoiname(String startpoiname) {
        this.startpoiname = startpoiname;
    }

    public String getStartlongitude() {
        return startlongitude;
    }

    public void setStartlongitude(String startlongitude) {
        this.startlongitude = startlongitude;
    }

    public String getStartlatitude() {
        return startlatitude;
    }

    public void setStartlatitude(String startlatitude) {
        this.startlatitude = startlatitude;
    }

    public String getStartlocation() {
        return startlocation;
    }

    public void setStartlocation(String startlocation) {
        this.startlocation = startlocation;
    }

    public String getEndpoiname() {
        return endpoiname;
    }

    public void setEndpoiname(String endpoiname) {
        this.endpoiname = endpoiname;
    }

    public String getEndlongitude() {
        return endlongitude;
    }

    public void setEndlongitude(String endlongitude) {
        this.endlongitude = endlongitude;
    }

    public String getEndlatitude() {
        return endlatitude;
    }

    public void setEndlatitude(String endlatitude) {
        this.endlatitude = endlatitude;
    }

    public String getEndlocation() {
        return endlocation;
    }

    public void setEndlocation(String endlocation) {
        this.endlocation = endlocation;
    }

    public String getCartime() {
        return cartime;
    }

    public void setCartime(String cartime) {
        this.cartime = cartime;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getInputtime() {
        return inputtime;
    }


    @Override
    public boolean equals(Object obj) {
//        System.out.println("equals:");
//        boolean flag = obj instanceof OrderBean;
//        if (flag == false) {
//            return false;
//        }
//        OrderBean emp = (OrderBean) obj;
//        if (this.getOrder_id() == emp.getOrder_id()) {
//            return true;
//        } else {
//            return false;
//        }



        boolean isok=true;
        if (obj == null)
            isok= false;
        if (getClass() != obj.getClass())
            isok=false;
        final OrderBean other = (OrderBean) obj;
        if(this.getOrder_id()!=other.getOrder_id())
            isok= false;
        System.out.println("orderBean equals:"+isok);
        return isok;
    }

    public void setInputtime(long inputtime) {
        this.inputtime = inputtime;
    }

    @Override
    public String toString() {
        return "OrderBean{" +
                "order_id=" + order_id +
                ", username='" + username + '\'' +
                ", userphone='" + userphone + '\'' +
                ", startpoiname='" + startpoiname + '\'' +
                ", startlongitude='" + startlongitude + '\'' +
                ", startlatitude='" + startlatitude + '\'' +
                ", startlocation='" + startlocation + '\'' +
                ", endpoiname='" + endpoiname + '\'' +
                ", endlongitude='" + endlongitude + '\'' +
                ", endlatitude='" + endlatitude + '\'' +
                ", endlocation='" + endlocation + '\'' +
                ", cartime='" + cartime + '\'' +
                ", num='" + num + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", car_id='" + car_id + '\'' +
                ", distance='" + distance + '\'' +
                ", state='" + state + '\'' +
                ", inputtime=" + inputtime +
                '}';
    }
}
