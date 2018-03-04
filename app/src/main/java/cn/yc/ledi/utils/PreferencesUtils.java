package cn.yc.ledi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import cn.yc.ledi.bean.CarBean;

/**
 * Created by Administrator on 2017-06-09.
 */

public class PreferencesUtils {
    private volatile static PreferencesUtils instance;
    SharedPreferences pre;
    Context mContext;

    public static PreferencesUtils getInstance() {
        if (instance == null) {
            synchronized (PreferencesUtils.class) {
                if (instance == null) {
                    instance = new PreferencesUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context mContext) {
        this.mContext = mContext;
        pre = mContext.getSharedPreferences("localhost_data.pref",
                Context.MODE_MULTI_PROCESS);
    }

    public void setCar_id(String car_id) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putString("car_id", car_id);
        editor.commit();
    }

    public void setIsLogin(boolean islogin) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("islogin", islogin);
        editor.commit();

    }

    public boolean getIsLogin() {

        return pre.getBoolean("islogin", false);
    }

    public String getCar_id() {

        return pre.getString("car_id", "");
    }

    public void setCurrentCarInfo(CarBean carInfo) {

        SharedPreferences.Editor editor = pre.edit();
        editor.putString("car_id", carInfo.getCar_id());
        editor.putString("carinfo", JSON.toJSON(carInfo).toString());
        editor.commit();
    }

    public CarBean getCurrentCarInfo() {
        String json = pre.getString("carinfo", "");
        CarBean carBean = null;
        if (!TextUtils.isEmpty(json)) {
            carBean = JSON.parseObject(json, CarBean.class);
        }
        return carBean;
    }

    public void setIsPlay(boolean isPlay) {
        SharedPreferences.Editor editor = pre.edit();
        editor.putBoolean("isplay", isPlay);
        editor.commit();
    }

    public boolean getIsPlay() {
        return pre.getBoolean("isplay", true);
    }
}
