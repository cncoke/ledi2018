package cn.yc.ledi.utils;

import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.listener.PublicReturnListener;


/**
 * Created by Administrator on 2017-06-09.
 */

public class DataUtils {
    private volatile static DataUtils instance;
    private static DbManager.DaoConfig daoConfig;
    private List<OrderBean> waitOrderList = new ArrayList<OrderBean>();
    DbManager db = x.getDb(getDaoConfig());

    public static DataUtils getInstance() {
        if (instance == null) {
            synchronized (DataUtils.class) {
                if (instance == null) {
                    instance = new DataUtils();
                }
            }
        }
        return instance;
    }

    public DbManager getDb() {
        return db;
    }

    public void setDb(DbManager db) {
        this.db = db;
    }

    public DbManager.DaoConfig getDaoConfig() {
        File file = new File(Environment.getExternalStorageDirectory().getPath());

        if (daoConfig == null) {
            daoConfig = new DbManager.DaoConfig()
                    .setDbName("ledi.db")   //设置数据库的名字
//                    .setDbDir(file)             //设置数据库存储的位置
                    .setDbVersion(1)            //设置数据的版本号
                    .setAllowTransaction(true)  //设置是否允许开启事务
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {//设置一个数据库版本升级的监听
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }

    public boolean saveOrder(final OrderBean orderBean) {
        //if (orderBean.getState().equals("0")) {
        //    SysApplication.getInstance().orderAdd(orderBean);
        // }


        Log.d("saveorder", "推送订单:" + orderBean.getStartpoiname());
        OrderBean bean = getOrderInfo(orderBean.getOrder_id());
        if (bean != null) {
            Log.d("saveorder", "不是订单:" + bean.getStartpoiname());
            if (!bean.getState().equals(orderBean.getState())) {
//                    updateOrderState(orderBean.getOrder_id(), orderBean.getState());
            }
            return false;
        } else {
//                System.out.println("time:"+orderBean.getInputtime()+" - local time:"+);

            ///收到新消息服务器回执
            ServerUtils.getInstance().setPushReceive(orderBean.getOrder_id(), new PublicReturnListener() {
                @Override
                public void onSuccess(Object data) {
                    try {
                        getDb().save(orderBean);
                        ReceiverUtils.getInstance().sendNewOrder(SysApplication.getInstance().getBaseContext(), orderBean);
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            });

            Log.d("saveorder", "新订单保存完成:" + orderBean.getStartpoiname());
        }


        return true;
    }

    public OrderBean getOrderInfo(int order_id) {
        OrderBean bean = null;
        try {
            bean = getDb().selector(OrderBean.class).where("order_id", "=", order_id).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
            System.out.printf("datautils  getorder info");
        }
        return bean;

    }

    public void updateOrderState(int order_id, String i) {
        try {

            /*
            WhereBuilder b = WhereBuilder.b();
            b.and("order_id", "=", order_id); //构造修改的条件
            KeyValue name = new KeyValue("state", i);
            getDb().update(OrderBean.class, b, name);*/

//            getDb().update(OrderBean.class,WhereBuilder.b().and("order_id","=",order_id),)
            WhereBuilder where = null;
            if (order_id > 0) {

                where = WhereBuilder.b("order_id", "=", order_id);
            }

            getDb().update(OrderBean.class, where, new KeyValue("state", i));
            if (i.equals("1")) {
                Intent intent = new Intent();
                intent.setAction(ReceiverUtils.NEWS_WAIT_SUCCESS_ORDER);
                SysApplication.getInstance().sendBroadcast(intent);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<OrderBean> getOrderList(String state, int num) {
        List<OrderBean> list = new ArrayList<OrderBean>();

        try {
            //.where(WhereBuilder.b("state", "=", state))
            list = getDb().selector(OrderBean.class).where(WhereBuilder.b("state", "=", state)).orderBy("order_id", true).limit(num).findAll();
        } catch (DbException e) {
            e.printStackTrace();
            System.out.println("DbException:" + e.getMessage());
        }
        return list;

    }

    public List<OrderBean> getWaiteSuccessOrder() {
        return waitOrderList;

    }

    public void saveWaitOrder(List<OrderBean> waitorder) {
        waitOrderList.clear();
        waitOrderList.addAll(waitorder);
//        for (OrderBean order : waitorder
//                ) {
//
//            saveOrder(order);
//
//        }


    }

    public void addWaitOrder(OrderBean orderBean) {
        waitOrderList.add(0, orderBean);
    }

    public CarBean getCarInfo(String car_id) {
        CarBean bean = null;
        if (!TextUtils.isEmpty(car_id)) {
            try {
                bean = getDb().selector(CarBean.class).where("car_id", "=", car_id).findFirst();
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
