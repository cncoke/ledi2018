package cn.yc.ledi.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;

import java.util.ArrayList;
import java.util.List;

import cn.yc.ledi.R;
import cn.yc.ledi.adapter.CommonAdapter;
import cn.yc.ledi.adapter.NewOrderHomeAdapter;
import cn.yc.ledi.base.Config;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.BaseResultBean;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.bean.ViewHolder;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.dialog.OrderMessageDialog;
import cn.yc.ledi.ui.PublicWebView;
import cn.yc.ledi.utils.DataUtils;
import cn.yc.ledi.utils.PublicUtils;
import cn.yc.ledi.utils.ReceiverUtils;
import cn.yc.ledi.utils.ServerUtils;
import cn.yc.ledi.utils.StatusBarUtils;
import cn.yc.ledi.widget.NoScrollListView;
import io.otherutils.tooles.SysLog;
import io.yc.library.httpokgo.model.BaseResult;
import io.yc.library.httpokgo.model.Response;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Fragment_home extends BaseFragment {
    View view;
    NewOrderHomeAdapter waitSuccessAdapter;
    CommonAdapter<OrderBean> newOrderAdapter;
    SwipeRefreshLayout swipeRefreshView;
    List<OrderBean> newOrderList = new ArrayList<OrderBean>();
    NoScrollListView listView;
    TextView tv_exit, tv_carname, tv_cardnumber, tv_ordernum, tv_balance, tv_enddate, tv_message, tv_blank_order_message;
    private FragmentManager manager;
    private FragmentTransaction ft;
    OrderMessageDialog orderDialog;
    private RecyclerView id_recyclerview;
    private PowerManager powerManager = null;
    private PowerManager.WakeLock wakeLock = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_home, null);
        initView(getActivity());
        initUserInfo();
//        initNewOrderReceiver();
        initWaitOrder();
        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        return view;


    }

    private void initNewOrderReceiver() {
        IntentFilter orderMessage = new IntentFilter();
        orderMessage.addAction(ReceiverUtils.NEWS_ORDER_MESSAGE);
        orderMessage.setPriority(100);
        getActivity().registerReceiver(orderMessageReceiver, orderMessage);

    }

    private BroadcastReceiver orderMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ReceiverUtils.NEWS_ORDER_MESSAGE)) {
                initWaitOrder();

            }
            //abortBroadcast();
        }
    };


    @Override
    public void initView(Context context) {
        super.initView(context);
        powerManager = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
        StatusBarUtils.setWindowStatusBarColor(getActivity(), R.color.common_title_button_pressed);
        manager = getFragmentManager();
        orderDialog = new OrderMessageDialog(getActivity());

        id_recyclerview = (RecyclerView) view.findViewById(R.id.id_recyclerview);
        listView = (NoScrollListView) view.findViewById(R.id.listView);
        tv_exit = (TextView) view.findViewById(R.id.tv_exit);
        tv_blank_order_message = (TextView) view.findViewById(R.id.tv_blank_order_message);
        tv_carname = (TextView) view.findViewById(R.id.tv_carname);
        tv_cardnumber = (TextView) view.findViewById(R.id.tv_cardnumber);
        tv_ordernum = (TextView) view.findViewById(R.id.tv_ordernum);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        tv_enddate = (TextView) view.findViewById(R.id.tv_enddate);
        tv_message = (TextView) view.findViewById(R.id.tv_message);
        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
        swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshView.setColorSchemeResources(R.color.public_button_normal, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUserInfo();
                initWaitOrder();
            }
        });


        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogManage.confirm("收车后将收不到消息?", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SysApplication.getInstance().exit();
                    }
                });
            }
        });


        newOrderAdapter = new CommonAdapter<OrderBean>(getActivity(), newOrderList, R.layout.item_home_news_order) {
            @Override
            public void convert(ViewHolder holder, final OrderBean orderBean) {
                System.out.println("CommonAdapter   orderbean:");
                if (orderBean != null) {

//                    holder.setText(R.id.tv_submitOrder, orderBean.getUserphone());
                    holder.getView(R.id.tv_submitOrder).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            orderDialog.setReturnListener(orderReturnListener);
//                            orderDialog.showDialog(orderBean);
                            onSubmitOrder(orderBean);

                        }
                    });
                    holder.setText(R.id.tv_startpoiname, PublicUtils.formatStartPoiName(orderBean.getStartpoiname()));
                    holder.setText(R.id.tv_endpoiname, PublicUtils.formatEndPoiName(orderBean.getEndpoiname()));

                }
            }
        };

        listView.setAdapter(newOrderAdapter);

    }

    private void initUserInfo() {

        ServerUtils.getInstance().getUserInfo(new ResultCallback<BaseResult<CarBean>>() {

            @Override
            public void onSuccess(BaseResult<CarBean> response) {
                swipeRefreshView.setRefreshing(false);
                settingView(response.getResult());
            }


            @Override
            public void onError(int code, String msg) {
                //super.onError(code, msg);
                swipeRefreshView.setRefreshing(false);
                dialogManage.alert(msg);

            }

            @Override
            public void onError(Response<BaseResult<CarBean>> response) {
                super.onError(response);
            }
        });


        SysApplication.getInstance().getCurrentLocation(onTrackListener);
    }

    OnTrackListener onTrackListener = new OnTrackListener() {
        @Override
        public void onLatestPointCallback(LatestPointResponse latestPointResponse) {
            super.onLatestPointCallback(latestPointResponse);
        }
    };

    public void settingView(final CarBean v) {
        if (v.getWaitorder().size() > 0) {
            DataUtils.getInstance().saveWaitOrder(v.getWaitorder());
        }
        tv_carname.setText(v.getCarname());
        tv_cardnumber.setText(v.getLicense_zh() + v.getLicense_city() + v.getLicense_num());
        tv_ordernum.setText(v.getOrdernum());
        tv_balance.setText(v.getBalance() + "");
        tv_enddate.setText(PublicUtils.formatDate("yyyy-MM-dd", Long.parseLong(v.getEnddate()) * 1000));

        if (v.getMessage() != null) {

            tv_message.setText(v.getMessage().getPost_excerpt());
            tv_message.setGravity(Gravity.LEFT);
            tv_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View e) {

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), PublicWebView.class);
                    intent.putExtra(Config.STRING_TITLE, v.getMessage().getPost_title());
                    if (!TextUtils.isEmpty(v.getMessage().getPost_source())) {
                        intent.putExtra(Config.STRING_URL, v.getMessage().getPost_source());
                    } else if (!TextUtils.isEmpty(v.getMessage().getPost_content())) {
                        intent.putExtra(Config.STRING_CONTENT, v.getMessage().getPost_content());
                    }
                    startActivity(intent);
                }
            });
        } else {
            tv_message.setGravity(Gravity.CENTER);
            tv_message.setText("暂无消息");
        }
    }

    public void initWaitOrder() {

        newOrderList.clear();
        List<OrderBean> list = DataUtils.getInstance().getOrderList("0", 5);
        if (list != null && list.size() > 0) {
            newOrderList.addAll(list);
            newOrderAdapter.notifyDataSetChanged();
            tv_blank_order_message.setVisibility(list.size() > 0 ? View.GONE : View.VISIBLE);
        }
    }

    public void onSubmitOrder(OrderBean orderBean) {
        if (orderBean == null) {

            List<OrderBean> list = DataUtils.getInstance().getOrderList("0", 1);
            if (list != null && list.size() > 0) {
                orderBean = list.get(0);
            } else {

                return;
            }
        }


        PublicUtils.vibrate((Activity) mContext, 200);
        ServerUtils.getInstance().submitOrder(orderBean.getOrder_id(), new ResultCallback<OrderBean>() {
            @Override
            public void onSuccess(OrderBean response) {
                super.onSuccess(response);
                OrderBean ob = DataUtils.getInstance().getOrderInfo(response.getOrder_id());
                orderDialog.showDialog(ob);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dialogManage.alert(msg);
            }
        });


        /*ServerUtils.getInstance().submitOrder(orderBean.getOrder_id(), new ResultCallback<OrderBean>() {



        }.setDialog(dialogManage.getDialog(getActivity())));*/
    }


    @Override
    public void onResume() {
        super.onResume();


        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getActivity().getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}