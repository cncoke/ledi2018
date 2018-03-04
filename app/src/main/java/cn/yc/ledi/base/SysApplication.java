package cn.yc.ledi.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.api.entity.LocRequest;
import com.baidu.trace.api.track.LatestPointRequest;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.ProcessOption;
import com.baidu.trace.model.PushMessage;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.mob.MobSDK;

import org.xutils.BuildConfig;
import org.xutils.HttpManager;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.service.NotificationServer;
import cn.yc.ledi.utils.NotificationUtil;
import cn.yc.ledi.utils.PreferencesUtils;
import cn.yc.ledi.utils.ReceiverUtils;
import cn.yc.ledi.utils.ServerUtils;
import io.yc.library.httpokgo.HttpRequestUtils;


/**
 * Created by Administrator on 2017/5/22.
 */

public class SysApplication extends Application {

    LBSTraceClient mTraceClient;
    private static SysApplication instance;
    private ArrayList<Activity> mList = new ArrayList<Activity>();
    private String car_id = "0";
    private String phoneTag = "";
    private List<OrderBean> orderMessages = new ArrayList<OrderBean>();
    private Trace mTrace;
    LocRequest locRequest;
    public BDLocation location;

    @Override
    public void onCreate() {
        super.onCreate();


        init();

    }

    public String getPhoneTag() {
        String tag = phoneTag;
        if (TextUtils.isEmpty(tag)) {
            tag = "no phone tag";//JPushInterface.getRegistrationID(this);
        }
        return tag;
    }

    public void setPhoneTag(String phoneTag) {
        this.phoneTag = phoneTag;
    }

    public static SysApplication getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }

//            PreferencesUtils.getInstance().setCar_id("");
            PreferencesUtils.getInstance().setIsLogin(false);
            NotificationUtil.getInstance().cancelAllNotification();
            traceClientStop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void traceClientStart() {
        if (!TextUtils.isEmpty(PreferencesUtils.getInstance().getCar_id())) {

            mTrace = new Trace(Config.BD_SERVICEID, PreferencesUtils.getInstance().getCar_id(), false);
            mTraceClient.startTrace(mTrace, mTraceListener);

            mTraceClient.startGather(mTraceListener);

        }


        CarBean carinfo = PreferencesUtils.getInstance().getCurrentCarInfo();
        /*
        RongIMClient.connect(carinfo.getRckey(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

                System.out.println("RongIMClient.connect   onTokenIncorrect");
            }

            @Override
            public void onSuccess(String s) {
                System.out.println("RongIMClient.connect   onSuccess:" + s);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                System.out.println("RongIMClient.connect   errorCode:" + errorCode);

            }
        });*/

    }

    public void traceClientStop() {
        if (mTrace != null && mTraceClient != null) {
            mTraceClient.stopTrace(mTrace, mTraceListener);
        }

    }

    public void getCurrentLocation(OnTrackListener entityListener) {
//        mTraceClient.queryRealTimeLoc(locRequest, entityListener);
        LatestPointRequest request = new LatestPointRequest(1, Config.BD_SERVICEID, "1");
        ProcessOption processOption = new ProcessOption();
        processOption.setNeedDenoise(true);
        processOption.setRadiusThreshold(100);
        request.setProcessOption(processOption);
        mTraceClient.queryLatestPoint(request, entityListener);
    }

    private void init() {
        instance = this;
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);
        mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.setInterval(2, 6);
       HttpRequestUtils.getInstance().init(this).setBaseurl(Config.BASE_API_URL_RELEASE);
        //HttpManagerUtils.getInstance().setBaseUrl(Config.BASE_API_URL_RELEASE).debug("ledi app:");
       /* HttpManagerUtils.getInstance().init(this);
        HttpManagerUtils.getInstance().debug(this.getApplicationInfo().packageName,true)
                .setBaseUrl(Config.BASE_API_URL_RELEASE)

        ;*/
        locRequest = new LocRequest(Config.BD_SERVICEID);
        ServerUtils.getInstance().init(this);
        NotificationUtil.getInstance().init(this);


        PreferencesUtils.getInstance().init(this);

        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=57babc54");


//        UpdateHelper.getInstance().init(getApplicationContext(), Color.parseColor("#29cc96"));
        Intent intent = new Intent(this, NotificationServer.class);
        startService(intent);
//        BaiduLocationUtils.getInstance().getLocation(this, new PublicReturnListener() {
//            @Override
//            public void onSuccess(Object data) {
//                BDLocation mlocation = (BDLocation) data;
//                location = mlocation;
//            }
//        });

//        RongIMClient.init(this);
//        RongIMClient.setOnReceiveMessageListener(new RongCloudReceiveMessageListene());


        MobSDK.init(this);

    }

//    public BDLocation getLocation() {
//        return location;
//    }
//
//    public void setLocation(BDLocation location) {
//        this.location = location;
//    }

    OnTraceListener mTraceListener = new OnTraceListener() {
        @Override
        public void onBindServiceCallback(int i, String s) {
            System.out.println("OnTraceListener:onBindServiceCallback:" + s);
        }

        // 开启服务回调
        @Override
        public void onStartTraceCallback(int status, String s) {
            System.out.println("OnTraceListener:onStartTraceCallback开启服务回调:" + s);
        }

        // 停止服务回调
        @Override
        public void onStopTraceCallback(int status, String s) {
            System.out.println("OnTraceListener:onStopTraceCallback停止服务回调:" + s);
        }

        // 开启采集回调
        @Override
        public void onStartGatherCallback(int status, String s) {
            System.out.println("OnTraceListener:onStartGatherCallback开启采集回调:" + s);
        }

        // 停止采集回调
        @Override
        public void onStopGatherCallback(int status, String s) {
            System.out.println("OnTraceListener:onStopGatherCallback停止采集回调:" + s);
        }

        // 推送回调
        @Override
        public void onPushCallback(byte messageNo, PushMessage s) {
            System.out.println("OnTraceListener:onPushCallback推送回调:" + s);
        }

        @Override
        public void onInitBOSCallback(int i, String s) {
            System.out.println("OnTraceListener:onInitBOSCallback:" + s);

        }


    };


    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public List<OrderBean> getOrderMessages() {
        return orderMessages;
    }

    public void setOrderMessages(List<OrderBean> orderMessages) {
        this.orderMessages = orderMessages;
    }

    public void orderAdd(OrderBean orderBean) {
//        if(getOrderMessages()!=null) {
//            getOrderMessages().add(orderBean);
//        }

        if (!getOrderMessages().contains(orderBean)) {
            getOrderMessages().add(orderBean);
            Intent intent = new Intent();
            intent.setAction(ReceiverUtils.NEWS_ORDER_MESSAGE);
            intent.putExtra(Config.PUBLIC_DATA, orderBean);
            getBaseContext().sendOrderedBroadcast(intent, null);
        }
    }


}
