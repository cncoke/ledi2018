package io.yc.library.httpokgo.callback;

/**
 * Created by Administrator on 2018-02-26.
 */

import android.app.Dialog;

import io.yc.library.httpokgo.callback.AbsCallback;
import io.yc.library.httpokgo.request.base.Request;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import io.yc.library.httpokgo.utils.OkLogger;
import okhttp3.Response;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/14
 * 描    述：默认将返回的数据解析成需要的Bean,可以是 BaseBean，String，List，Map
 * 修订历史：
 * ================================================
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;
    private Dialog dialog;


    public Dialog getDialog() {
        return dialog;
    }

    public JsonCallback setDialog(Dialog dialog) {
        this.dialog = dialog;
        return  this;
    }

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        // 主要用于在所有请求之前添加公共的请求头或请求参数
        // 例如登录授权的 token
        // 使用的设备信息
        // 可以随意添加,也可以什么都不传
        // 还可以在这里对所有的参数进行加密，均在这里实现
        /*request.headers("header1", "HeaderValue1")//
                .params("params1", "ParamsValue1")//
                .params("token", "3215sdf13ad1f65asd4f3ads1f");*/
        showProgress();
    }

    /**
     * 展示进度框
     */
    private void showProgress() {

        if (dialog!=null  ) {
            dialog.show();
        }
    }


    /**
     * 取消进度框
     */
    private void dismissProgress() {

        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onCacheSuccess(io.yc.library.httpokgo.model.Response<T> response) {
        super.onCacheSuccess(response);
        dismissProgress();
        onSuccess(response.body());
    }
    public void onSuccess(T response){


    }

    @Override
    public void onSuccess(io.yc.library.httpokgo.model.Response<T> response) {
        dismissProgress();
        onSuccess(response.body());
    }

    @Override
    public void onError(io.yc.library.httpokgo.model.Response<T> response) {
        super.onError(response);
        dismissProgress();
        Throwable throwable = response.getException();
        if (throwable instanceof UnknownHostException){
            onError(response.code(),"网络错误");
        }

    }
    public void onError(int code,String msg){

    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,以下给出的时模板代码,实际使用根据需要修改
     */
    @Override
    public T convertResponse(Response response) throws Throwable {

        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用
        // 重要的事情说三遍，不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }
}