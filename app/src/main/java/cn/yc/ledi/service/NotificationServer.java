package cn.yc.ledi.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.yc.ledi.utils.NotificationUtil;
import cn.yc.ledi.utils.ReceiverUtils;

/**
 * Created by Administrator on 2017-06-20.
 */

public class NotificationServer extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter orderMessage = new IntentFilter();
        orderMessage.addAction(ReceiverUtils.NEWS_ORDER_MESSAGE);
        orderMessage.setPriority(999);
        registerReceiver(newOrderMessageNotification, orderMessage);
    }

    private void initEaseLogin() {

//        EMClient.getInstance().addConnectionListener(new MyConnectionListener(getApplicationContext()));
//        EMClient.getInstance().chatManager().addMessageListener(new MessageListener());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        initEaseLogin();
//        showNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification() {
        NotificationUtil.getInstance().postCustomNotification();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }


    BroadcastReceiver newOrderMessageNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ReceiverUtils.NEWS_ORDER_MESSAGE)) {
//                showOrderDialog();
               // NotificationUtil.getInstance().postCustomNotification("收到新叫车信息,点击查看 Not service",true);

            }
        }
    };


    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, NotificationServer.class); // 销毁时重新启动Service
        this.startService(localIntent);
        super.onDestroy();
    }
}
