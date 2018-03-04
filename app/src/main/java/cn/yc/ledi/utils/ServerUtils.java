package cn.yc.ledi.utils;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yc.ledi.BuildConfig;
import cn.yc.ledi.bean.BaseResultBean;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.bean.GoldRecordBean;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.listener.PublicReturnListener;
import io.yc.library.httpokgo.HttpRequestUtils;
import io.yc.library.httpokgo.model.BaseResult;


/**
 * Created by Administrator on 2017-05-27.
 */

public class ServerUtils {

    private volatile static ServerUtils instance;
    public Context mContext;

    public void init(Context context) {
        this.mContext = context;
    }

    ;


    public static ServerUtils getInstance() {
        if (instance == null) {
            synchronized (ServerUtils.class) {
                if (instance == null) {
                    instance = new ServerUtils();
                }
            }
        }
        return instance;
    }

    private HashMap getUserToken() {
        CarBean carBean = null;
        HashMap<String, String> map = new HashMap<>();
        //carBean = DataUtils.getInstance().getDb().findFirst(CarBean.class);

        carBean = DataUtils.getInstance().getCarInfo(PreferencesUtils.getInstance().getCar_id());
        if (carBean != null) {
            map.put("token", carBean.getToken());
        }
        return map;
    }

    public void getUserInfo(final ResultCallback<BaseResult<CarBean>> resultCallback) {

        HashMap<String, String> map = getUserToken();
        String url = "user/getUserInfo";

        HttpRequestUtils.post(url)
                .cacheTime(1000 * 10)
                .params(formateParams(map))
                .execute(new ResultCallback<BaseResult<CarBean>>() {
                    @Override
                    public void onSuccess(BaseResult<CarBean> response) {
                        if (response.getResult().getWaitorder() != null && response.getResult().getWaitorder().size() > 0) {
                            DataUtils.getInstance().saveWaitOrder(response.getResult().getWaitorder());
                        }
                        resultCallback.onSuccess(response);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        resultCallback.onError(code,msg);
                    }
                });
        /*HttpManager.post(url)
                .cacheKey("getuserinfo-" + url)
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .cacheTime(1000 * 10)
                .params(map).execute(new ResultCallback<BaseResultBean<CarBean>>() {
            @Override
            public void onSuccess(BaseResultBean<CarBean> response) {

            }

        });*/
    }

    /*public <T> void getUserInfo(final DialogManage dialogManage, Class<T> cls, final OnResult<T> onResult) {


        HashMap<String, String> map = getUserToken();
        String url = Config.GET_APIURL() + "user/getUserInfo";
        sendServiceApi(dialogManage, url, map, new OnResult<T>() {
            @Override
            public void OnSuccess(T cls) {
                CarBean carBean = (CarBean) cls;

//                DataUtils.getInstance().updateOrderState(0, "2");
                if (carBean.getWaitorder() != null && carBean.getWaitorder().size() > 0) {
                    DataUtils.getInstance().saveWaitOrder(carBean.getWaitorder());
                }
                onResult.OnSuccess(cls);
            }


            @Override
            public void OnError(String msg) {

                onResult.OnError(msg);
            }
        }, cls);


    }*/

    public void register(Map<String,String> map, ResultCallback<BaseResultBean<String>> resultCallback) {

        String url = "user/register";


        HttpRequestUtils.post(url)
                .cacheTime(1000 * 10)
                .params(formateParams(map))
                .execute(resultCallback);
        /*HttpManager.post(url)
                .cacheKey(url)
                .cacheTime(1000 * 10)
                .cacheMode(CacheMode.IF_NONE_CACHE_REQUEST)
                .params(map).execute(resultCallback);*/

    }

    private Map<String,String > formateParams(Map<String,String > map ){
        //HttpParams params = new HttpParams();
        //params.put(map);
        map.put("versioncode", BuildConfig.VERSION_CODE+"");
        return map;
    }
    public <T> void login(Map<String, String> map, ResultCallback<T> resultCallback) {
        // HttpManager.post(Config.GET_APIURL() + "user/login").params(map).execute(resultCallback);
        HttpRequestUtils.post("user/login").params(formateParams(map)).execute(resultCallback);
    }
/*
    public <T> void login(final DialogManage dialogManage, Map<String, String> map, Class<CarBean> cls, final Result<T> onResult) {
        String url = Config.GET_APIURL() + "user/login";
        if (dialogManage != null) {
            dialogManage.loading();
        }

        OkGo.<BaseResultBean<T>>post(url).cacheMode(CacheMode.NO_CACHE).params(map).execute(new AbsCallback<BaseResultBean<T>>() {


            @Override
            public BaseResultBean<T> convertResponse(okhttp3.Response response) throws Throwable {
                return null;
            }

            @Override
            public void onSuccess(Response<BaseResultBean<T>> response) {
                onResult.OnComplete(response.body());
            }

            @Override
            public void onError(Response<BaseResultBean<T>> response) {
                super.onError(response);
            }
        });
        //sendServiceApi(dialogManage, url, map, onResult, cls);
    }

    private void httpService(String url, Map<String, String> map, final HttpUtils.XCallBack callback) {
        Log.e("http", url);
        Log.e("http", map.toString());
        HttpUtils.XCallBack xCallBack = new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                callback.onResponse(result);
            }

            @Override
            public void onFail(String result) {
                callback.onFail(result);

            }
        };
//        HttpUtils.getInstance().post(url, map, xCallBack);
        HttpUtils.getInstance().postCache(url, map, true, 50000, xCallBack);

    }
    private <T> void sendServiceApi(final DialogManage dialogManage, String url, Map<String, String> map, final OnResult<T> onResult, final Class<T> cls) {

        httpService(url, map, new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                Log.e("http", result);

                if (dialogManage != null) {
                    dialogManage.cancel();
                }

                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("status") < 0) {

                    if (jsonObject.getInteger("status") == -1000) {
                        ReceiverUtils.getInstance().sendOtherLogin(mContext);
                    } else {
                        onResult.OnError(jsonObject.getString("msg").toString());
                    }
                } else {
                    if (jsonObject.getJSONObject("result") != null && !jsonObject.getJSONObject("result").equals("")) {
                        onResult.OnSuccess(PublicUtils.resultToBean(jsonObject, cls));
                    } else {
                        try {
                            T user = newTclass(cls);

                            onResult.OnSuccess(user);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }

            @Override
            public void onFail(String result) {
                if (dialogManage != null) {
                    dialogManage.cancel();
                }
                onResult.OnError(result);
            }
        });

    }
*/


    public void submitOrder(final int order_id, final ResultCallback<OrderBean> onResult) {
        String url =   "order/submitOrder";
        HashMap<String, String> map = getUserToken();
        map.put("order_id", order_id + "");


        HttpRequestUtils.post(url)
                .params(formateParams(map))
                .execute(new ResultCallback<BaseResultBean<OrderBean>>() {
                    @Override
                    public void onSuccess(BaseResultBean<OrderBean> response) {
                        DataUtils.getInstance().updateOrderState(order_id, "1");
                        onResult.onSuccess(response.getResult());
                    }


                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        DataUtils.getInstance().updateOrderState(order_id, "2");
                        onResult.onError(code,msg);
                    }


                });

    }
/*
    public <T> void getList(String url, Map<String, String> map, final Class<T> cls, final OnResult<List<T>> onResult) {

        httpService(url, map, new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (checkStatus(jsonObject) != null) {
                    onFail(checkStatus(jsonObject));

                } else {
                    String resultjsonObject = jsonObject.getJSONArray("result").toString();
                    System.out.println("11111111111111:" + resultjsonObject);
                    if (!TextUtils.isEmpty(resultjsonObject)) {


                        List<OrderBean> l = JSON.parseArray(resultjsonObject, OrderBean.class);

                        for (OrderBean o : l) {
                            System.out.println("orderBean:" + o);
                        }
                        // onResult.OnSuccess(l);
//                        onResult.OnSuccess(PublicUtils.resultToBean(jsonObject, cls));
                    }
                }
            }

            @Override
            public void onFail(String result) {

                System.out.println("         **************" + result);
            }
        });
    }*/

    public void setOrderState(String order_id, int state, final ResultCallback<BaseResultBean> onResult) {
        String url =  "order/setState";
        HashMap<String, String> map = getUserToken();
        map.put("order_id", order_id);
        map.put("state", state + "");
        HttpRequestUtils.post(url).params(formateParams(map)).execute(new ResultCallback<BaseResultBean<String>>() {
            @Override
            public void onSuccess(BaseResultBean<String> response) {


            }
        });
        /*
        httpService(url, map, new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (checkStatus(jsonObject) != null) {
                    onFail(checkStatus(jsonObject));
                } else {

                    onResult.OnSuccess(new BaseResultBean());
                }
            }

            @Override
            public void onFail(String result) {

            }
        });*/

    }

    public void getOrderList(int state, int pageNum, final ResultCallback<List<OrderBean>> resultCallback) {
        String url =  "order/getUserOrder";
        HashMap<String, String> map = getUserToken();
        map.put("p", pageNum + "");
//        map.put("state",state+"");
        HttpRequestUtils.post(url)

                .params(formateParams(map))
                .cacheTime(1000 * 5)
                .execute(new ResultCallback<BaseResultBean<List<OrderBean>>>() {


                    @Override
                    public void onSuccess(BaseResultBean<List<OrderBean>> response) {
                        resultCallback.onSuccess(response.getResult());
                    }

                });
    }

    /*public <T> void getOrderList(int state, int pageNum, final OnResult<List<OrderBean>> onResult) {
        String url = Config.GET_APIURL() + "order/getUserOrder";
        HashMap<String, String> map = getUserToken();
        map.put("p", pageNum + "");
//        map.put("state",state+"");
        httpService(url, map, new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (checkStatus(jsonObject) != null) {
                    onFail(checkStatus(jsonObject));
                } else {
                    String resultjsonObject = jsonObject.getJSONArray("result").toString();
                    if (!TextUtils.isEmpty(resultjsonObject)) {


                        List<OrderBean> l = JSON.parseArray(resultjsonObject, OrderBean.class);


                        onResult.OnSuccess(l);
//                        onResult.OnSuccess(PublicUtils.resultToBean(jsonObject, cls));
                    }
                }
            }

            @Override
            public void onFail(String result) {

            }
        });
    }*/

    private String checkStatus(JSONObject jsonObject) {
        String result = null;
        if (jsonObject.getInteger("status") < 0) {
            if (jsonObject.getInteger("status") == -1000) {
                ReceiverUtils.getInstance().sendOtherLogin(mContext);
            } else {
                result = jsonObject.getString("msg").toString();
            }
        }
        return result;
    }

    public void getGoldRecord(int pageNum,int type, final ResultCallback<List<GoldRecordBean>> onResult) {


        String url =  "order/getGoldRecord";
        HashMap<String, String> map = getUserToken();
        map.put("p", pageNum + "");
        map.put("type", type + "");

        HttpRequestUtils.post(url)
                .cacheTime(1000 * 5)
                .cacheKey(url+type)
                .params(formateParams(map)).execute(new ResultCallback<BaseResult<List<GoldRecordBean>>>() {
            @Override
            public void onSuccess(BaseResult<List<GoldRecordBean>> response) {
                onResult.onSuccess(response.getResult());
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                onResult.onError(code,msg);
            }
        });
        /*
        httpService(url, map, new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (checkStatus(jsonObject) != null) {
                    onFail(checkStatus(jsonObject));
                } else {
                    String resultjsonObject = jsonObject.getJSONArray("result").toString();
                    if (!TextUtils.isEmpty(resultjsonObject)) {
                        List<GoldRecordBean> l = JSON.parseArray(resultjsonObject, GoldRecordBean.class);
                        onResult.OnSuccess(l);
                    }
                }
            }

            @Override
            public void onFail(String result) {

            }
        });*/
    }

    public void setPushReceive(int order_id, final PublicReturnListener listener) {

        String url =   "order/setpushreceive";
        HashMap<String, String> map = getUserToken();
        map.put("order_id", order_id + "");

        HttpRequestUtils.post(url)
                .params(formateParams(map))

                .execute(new ResultCallback<BaseResultBean<String>>() {
                    @Override
                    public void onSuccess(BaseResultBean<String> response) {
                        listener.onSuccess(1);
                    }
                });
       /* httpService(url, map, new HttpUtils.XCallBack() {
            @Override
            public void onResponse(String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                if (jsonObject.getInteger("status") < 0) {

                    if (jsonObject.getInteger("status") == -1000) {
                        ReceiverUtils.getInstance().sendOtherLogin(mContext);
                    }
                } else {

                    listener.onSuccess(1);
                }
            }

            @Override
            public void onFail(String result) {

            }
        });*/

    }

    public void updateToken(String mobilephone, ResultCallback<CarBean> onResult) {
        String url = "user/updateToken";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobilephone", mobilephone + "");

        HttpRequestUtils.post(url)
                .cacheTime(1000 * 3)
                .params(formateParams(map))

                .execute(onResult);


    }

    public void updatePassword(Map<String, String> map, ResultCallback<CarBean> onResult) {
        String url =  "user/updatePassword";

        HttpRequestUtils.post(url)
                .cacheTime(1000 * 3)
                .params(formateParams(map))
                .execute(onResult);
    }


}
