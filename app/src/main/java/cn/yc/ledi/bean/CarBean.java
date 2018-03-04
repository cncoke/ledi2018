package cn.yc.ledi.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017-06-09.
 */

@Table(name="car")
public class CarBean  implements Serializable {
    @Column(name = "car_id",isId = true,autoGen = false)
    private String car_id;
    @Column(name = "license_city")
    private String license_city;
    @Column(name = "state")
    private String state;
    @Column(name = "carname")
    private String carname;
    @Column(name = "cardnumber")
    private String cardnumber;
    @Column(name = "license_zh")
    private String license_zh;
    @Column(name = "cartype")
    private String cartype;
    @Column(name = "password")
    private String password;
    @Column(name = "city")
    private String city;
    @Column(name = "license_num")
    private String license_num;
    @Column(name = "phonetag")
    private String phonetag;
    @Column(name = "mobilephone")
    private String mobilephone;
    @Column(name = "token")
    private String token;
    @Column(name = "address")
    private String address;
    @Column(name = "regdate")
    private String regdate;
    @Column(name = "province")
    private String province;
    @Column(name = "openid")
    private String openid;
    @Column(name = "district")
    private String district;
    @Column(name = "enddate")
    private String enddate;
    @Column(name="rckey")
    private String rckey;

    private String ordernum;
    private String balance;

    private List<OrderBean> waitorder;
    private MessageBean message;


    private String tikutype;

    public String getTikutype() {
        return tikutype;
    }

    public void setTikutype(String tikutype) {
        this.tikutype = tikutype;
    }

    public String getRckey() {
        return rckey;
    }

    public void setRckey(String rckey) {
        this.rckey = rckey;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public List<OrderBean> getWaitorder() {
        return waitorder;
    }

    public void setWaitorder(List<OrderBean> waitorder) {
        this.waitorder = waitorder;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getOrdernum() {
        return ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getLicense_city() {
        return license_city;
    }

    public void setLicense_city(String license_city) {
        this.license_city = license_city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getLicense_zh() {
        return license_zh;
    }

    public void setLicense_zh(String license_zh) {
        this.license_zh = license_zh;
    }

    public String getCartype() {
        return cartype;
    }

    public void setCartype(String cartype) {
        this.cartype = cartype;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLicense_num() {
        return license_num;
    }

    public void setLicense_num(String license_num) {
        this.license_num = license_num;
    }

    public String getPhonetag() {
        return phonetag;
    }

    public void setPhonetag(String phonetag) {
        this.phonetag = phonetag;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    @Override
    public String toString() {
        return "CarBean{" +
                "car_id='" + car_id + '\'' +
                ", license_city='" + license_city + '\'' +
                ", state='" + state + '\'' +
                ", carname='" + carname + '\'' +
                ", cardnumber='" + cardnumber + '\'' +
                ", license_zh='" + license_zh + '\'' +
                ", cartype='" + cartype + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", license_num='" + license_num + '\'' +
                ", phonetag='" + phonetag + '\'' +
                ", mobilephone='" + mobilephone + '\'' +
                ", token='" + token + '\'' +
                ", address='" + address + '\'' +
                ", regdate='" + regdate + '\'' +
                ", province='" + province + '\'' +
                ", openid='" + openid + '\'' +
                ", district='" + district + '\'' +
                ", enddate='" + enddate + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", balance=" + balance +
                '}';
    }
}
