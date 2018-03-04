package cn.yc.ledi.base;


import cn.yc.ledi.BuildConfig;

/**
 * Created by Administrator on 2017/5/22.
 */

public class Config {

    final public static int BD_SERVICEID = 136210;
    final public static String PUBLIC_DATA = "app_data";
    final public static String BASE_API_URL_TEST = "http://api.ledi.appcms.top/v3/api/";
    final public static String BASE_API_URL_RELEASE = "http://192.168.1.109/ledi/v3/api/";



    final public static String[] cityen = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    final public static String ACTION_NEWS_ORDER = "news_order_message";
    public static String STRING_CONTENT = "string_content";
    public static String STRING_URL = "string_url";
    public static String STRING_TITLE = "string_title";
    public static String ACTION_OTHERLOGIN = "cn.yc.app.receiver.otherlogin";
    public static String NOTIFICATION_BIT="notification_bit";

    public static String GET_APIURL(){
        String url= BASE_API_URL_RELEASE;
        if (BuildConfig.BUILD_TYPE.equals("debug")){
            url= BASE_API_URL_TEST;
        }

//        if(PublicUtils.getSha1(SysApplication.getInstance().getApplicationContext()).equals("E8:10:45:83:F8:02:85:6E:B2:6D:A9:09:EC:EC:89:D2:96:F7:2F:B2")){
//            url=BASE_API_URL_RELEASE;
//        }
        return url;

    }
}
