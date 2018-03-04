package cn.yc.ledi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.dialog.OrderMessageDialog;
import cn.yc.ledi.listener.PublicReturnListener;
import cn.yc.ledi.ui.GoldRecordActivity;
import cn.yc.ledi.ui.LoginActivity;
import cn.yc.ledi.utils.DataCleanManager;
import cn.yc.ledi.utils.PreferencesUtils;
import cn.yc.ledi.utils.ReceiverUtils;
import io.otherutils.dialog.DialogManage;


/**
 * Created by Administrator on 2017-06-22.
 */

public class FragmentMainActivity extends BaseFragmentActivity {

    OrderMessageDialog orderDialog;
    DialogManage dialogManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String car_id = PreferencesUtils.getInstance().getCar_id();
        if (TextUtils.isEmpty(car_id)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        initView();
        initNewOrderReceiver();
//
        initBaiduYing();

        LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // 未打开位置开关，可能导致定位失败或定位不准，提示用户或做相应处理
            dialogManage.alert("未打开位置开关，可能导致定位失败或定位不准");
        }
//        Intent intent = new Intent(this, ActivityBlank.class);
//        startActivity(intent);
    }



    private void initNewOrderReceiver() {
        IntentFilter orderMessage = new IntentFilter();
        orderMessage.addAction(ReceiverUtils.NEWS_ORDER_MESSAGE);
//        orderMessage.addAction(ReceiverUtils.NEWS_WAIT_SUCCESS_ORDER);
        orderMessage.setPriority(98);
        registerReceiver(orderMessageReceiver, orderMessage);

    }

    private BroadcastReceiver orderMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ReceiverUtils.NEWS_ORDER_MESSAGE)) {
//                OrderBean orderBean = (OrderBean) intent.getSerializableExtra(Config.PUBLIC_DATA);
//                showOrderDialog(orderBean);

                if (mTabHost.getCurrentTab() == 0) {
                    fragment1.initWaitOrder();
                }
            } else if (action.equals(ReceiverUtils.NEWS_WAIT_SUCCESS_ORDER)) {

                if (mTabHost.getCurrentTab() == 0) {
                    fragment1.initWaitOrder();
                }
//                initWaitOrder();
            }

            abortBroadcast();
        }
    };

    //    控件初始化控件
    private void initView() {
        dialogManage = new DialogManage(this);


//        UpdateHelper.getInstance().autoUpdate(this.getPackageName(), false, 1000 * 300);

    }


    private void showOrderDialog(OrderBean orderBean) {
//        if (!orderDialog.isShowing() && SysApplication.getInstance().getOrderMessages().size() > 0) {
        orderDialog = new OrderMessageDialog(this);
        orderDialog.setReturnListener(orderReturnListener);
        orderDialog.showDialog(orderBean);

//        }
    }

    ///点击抢单弹窗中的关闭后，回调；
    PublicReturnListener orderReturnListener = new PublicReturnListener() {
        @Override
        public void onSuccess(Object data) {
            int bit = (int) data;
            if (bit == 1) {

//                showOrderDialog(orderBean);
            }

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(orderMessageReceiver);
    }

    public void onShowOrder(View v) {
        mTabHost.setCurrentTab(1);
    }

    public void onTitleLeftLayout(View v) {
        mTabHost.setCurrentTab(0);
    }

    public void onUnLoginService(View v) {

        dialogManage.confirm("收车后将收不到消息?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SysApplication.getInstance().exit();
            }
        });
    }

    public void onUpdate(View v) {

//        UpdateHelper.getInstance().manualUpdate(getPackageName());
    }

    public void onCleanData(View v) {
        DataCleanManager.cleanInternalCache(this);
//        HttpManagerUtils.clearCache();
        dialogManage.alert("清理完成!");
    }

    public void onGoldRecord(View v) {
        Intent intent = new Intent(this, GoldRecordActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (isExit == false) {
//                isExit = true;
//                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//                if (!hasTask) {
//                    tExit.schedule(task, 2000);
//                }
//            } else {
//
//                // if (!isWork) {
//
//                finish();
//                System.exit(0);
//                // } else {
//                // dialog.alert("请收车后再退出");
//                // }
//            }
        } else if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {


            if (event.getRepeatCount() == 0) {
                fragment1.onSubmitOrder(null);
            }
        }
        return false;
    }
}
