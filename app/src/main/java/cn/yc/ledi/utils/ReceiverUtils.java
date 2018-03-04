package cn.yc.ledi.utils;

import android.content.Context;
import android.content.Intent;

import cn.yc.ledi.base.Config;
import cn.yc.ledi.bean.OrderBean;

/**
 * Created by Administrator on 2017-06-15.
 */

public class ReceiverUtils {
    final public static String NEWS_ORDER_MESSAGE = "news_order_message";
    public static final String NEWS_WAIT_SUCCESS_ORDER = "news_wait_success_order";
    private volatile static ReceiverUtils instance;


    public static ReceiverUtils getInstance() {
        if (instance == null) {
            synchronized (NotificationUtil.class) {
                if (instance == null) {
                    instance = new ReceiverUtils();
                }
            }
        }
        return instance;
    }

    public void sendNewOrder(Context mContext, OrderBean orderBean){
        Intent intent = new Intent();
        intent.setAction(ReceiverUtils.NEWS_ORDER_MESSAGE);
        intent.putExtra(Config.PUBLIC_DATA,orderBean);
//        mContext.sendOrderedBroadcast(intent, null);
        mContext.sendBroadcast(intent);
        MttsUtils.getInstance().playCallInfo(PublicUtils.formatStartPoiName(orderBean.getStartpoiname()) + PublicUtils.formatEndPoiName(orderBean.getEndpoiname()));

    }
    public void sendOtherLogin(Context mContext) {

        Intent intent = new Intent();
        intent.setAction(Config.ACTION_OTHERLOGIN);
        mContext.sendBroadcast(intent);
    }

}
