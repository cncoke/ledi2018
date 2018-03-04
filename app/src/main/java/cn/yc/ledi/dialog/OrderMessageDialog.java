package cn.yc.ledi.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.yc.ledi.R;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.listener.PublicReturnListener;
import cn.yc.ledi.utils.DataUtils;
import cn.yc.ledi.utils.PublicUtils;
import cn.yc.ledi.utils.ReceiverUtils;
import cn.yc.ledi.utils.ServerUtils;

/**
 * Created by Administrator on 2017-06-15.
 */

public class OrderMessageDialog extends Dialog {
    private Context mContext;
    private TextView tv_distance, tv_startpoiname, tv_endpoiname, tv_submitOrder, tv_username, tv_userphone, tv_closebutton, tv_showMessage;
    private LinearLayout ll_callphone, ll_userinfo, ll_usermessage;
    private OrderBean orderBean;
    private PublicReturnListener returnListener;
    private ProgressBar load_progress;

    public OrderMessageDialog(Context context) {
        super(context, R.style.dialog);
        this.mContext = context;

    }

    public OrderMessageDialog(Context context, int themeResId) {
        super(context, R.style.dialog);
        this.mContext = context;
    }

    protected OrderMessageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {

        setContentView(R.layout.window_neworder);
        setCancelable(false);

        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_startpoiname = (TextView) findViewById(R.id.tv_startpoiname);
        tv_endpoiname = (TextView) findViewById(R.id.tv_endpoiname);
        tv_submitOrder = (TextView) findViewById(R.id.tv_submitOrder);
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_userphone = (TextView) findViewById(R.id.tv_userphone);
        tv_closebutton = (TextView) findViewById(R.id.tv_closebutton);
        tv_showMessage = (TextView) findViewById(R.id.tv_showMessage);
        ll_callphone = (LinearLayout) findViewById(R.id.ll_callphone);
        ll_userinfo = (LinearLayout) findViewById(R.id.ll_userinfo);
        ll_usermessage = (LinearLayout) findViewById(R.id.ll_usermessage);
        load_progress = (ProgressBar) findViewById(R.id.load_progress);
        tv_closebutton.setOnClickListener(new clickListener());
        ll_callphone.setOnClickListener(new clickListener());
//        tv_submitOrder.setOnClickListener(new clickListener());
        tv_submitOrder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN: //手指按下
                        onSubmitOrder();
                        break;

                }
                return true;
            }
        });

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = mContext.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6

        dialogWindow.setAttributes(lp);
    }

    public void onSubmitOrder() {
        PublicUtils.vibrate((Activity) mContext, 200);
        setActivity(1);
        ServerUtils.getInstance().submitOrder(orderBean.getOrder_id(),  orderBeanOnResult);
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            int id = v.getId();
            switch (id) {
                case R.id.tv_closebutton:
                    setActivity(4);
                    if (SysApplication.getInstance().getOrderMessages() != null) {
                        //   SysApplication.getInstance().getOrderMessages().remove(orderBean);
                    }
//                    if(orderBean!=null && orderBean.getState()!=null && orderBean.getState().equals("0")) {
//                        DataUtils.getInstance().updateOrderState(orderBean.getOrder_id(), "-1");
//                    }
                    dismiss();
                    if (returnListener != null) {
                        returnListener.onSuccess(1);
                    }
                    break;
                case R.id.ll_callphone:///拨打电话

                    if (!TextUtils.isEmpty(orderBean.getUserphone())) {
                        PublicUtils.callPhone(mContext, orderBean.getUserphone());
                    }
                    break;
                case R.id.tv_submitOrder:///抢单


                    break;
            }
        }

    }

    private void setActivity(int n) {


        switch (n) {
            case 1:
                load_progress.setVisibility(View.VISIBLE);
                tv_submitOrder.setVisibility(View.GONE);
                break;
            case 2:
                load_progress.setVisibility(View.GONE);
                tv_submitOrder.setVisibility(View.GONE);
                ll_usermessage.setVisibility(View.GONE);
                ll_userinfo.setVisibility(View.VISIBLE);
                break;
            case 3:
                load_progress.setVisibility(View.GONE);
                tv_submitOrder.setVisibility(View.GONE);
                ll_userinfo.setVisibility(View.GONE);
                ll_usermessage.setVisibility(View.VISIBLE);
                break;
            case 4:

                load_progress.setVisibility(View.GONE);
                tv_submitOrder.setVisibility(View.VISIBLE);
                ll_usermessage.setVisibility(View.VISIBLE);
                tv_showMessage.setText(mContext.getResources().getText(R.string.order_alert_text));
                ll_userinfo.setVisibility(View.GONE);
                break;
        }


    }

    ResultCallback<OrderBean> orderBeanOnResult = new ResultCallback<OrderBean>() {


        @Override
        public void onSuccess(OrderBean cls) {
            setActivity(2);
            DataUtils.getInstance().addWaitOrder(orderBean);
            orderBean.setUserphone(cls.getUserphone());
            orderBean.setState("1");
            tv_userphone.setText(cls.getUserphone());

            Intent intent = new Intent();
            intent.setAction(ReceiverUtils.NEWS_WAIT_SUCCESS_ORDER);
            mContext.sendBroadcast(intent);
        }


        @Override
        public void onError(int code, String msg) {
            super.onError(code, msg);
            setActivity(3);
            tv_showMessage.setText(msg);
        }


    };

    public void showDialog(OrderBean info) {
        this.orderBean = info;
        if (orderBean != null) {

            SysApplication.getInstance().getOrderMessages().add(orderBean);
            show();
            tv_distance.setText(PublicUtils.formatDistance(orderBean.getDistance()));
            tv_startpoiname.setText(PublicUtils.formatStartPoiName(orderBean.getStartpoiname()));
            tv_endpoiname.setText(PublicUtils.formatEndPoiName(orderBean.getEndpoiname()));
            tv_username.setText(orderBean.getUsername());
            tv_userphone.setText(orderBean.getUserphone());
            tv_distance.setText(orderBean.getDistance());
            setActivity(2);

        }

    }

    public PublicReturnListener getReturnListener() {
        return returnListener;
    }

    public void setReturnListener(PublicReturnListener returnListener) {
        this.returnListener = returnListener;
    }
}
