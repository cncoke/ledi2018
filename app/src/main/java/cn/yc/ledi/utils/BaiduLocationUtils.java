package cn.yc.ledi.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import cn.yc.ledi.listener.LocationListener;
import cn.yc.ledi.listener.PublicReturnListener;

/**
 * Created by Administrator on 2017-06-08.
 */

public class BaiduLocationUtils {

    private volatile static BaiduLocationUtils instance;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener;
    public BDLocation location;

    public static BaiduLocationUtils getInstance() {
        if (instance == null) {
            synchronized (BaiduLocationUtils.class) {
                if (instance == null) {
                    instance = new BaiduLocationUtils();
                }
            }
        }
        return instance;
    }

    public void getLocation(Context mContext,PublicReturnListener publicReturnListener) {


        mLocationClient = new LocationClient(mContext);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000*30;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        myListener = new LocationListener(publicReturnListener);
        mLocationClient.setLocOption(option);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        mLocationClient.start();


    }


}
