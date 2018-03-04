/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.yc.library.httpokgo;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import io.yc.library.httpokgo.cache.CacheEntity;
import io.yc.library.httpokgo.cache.CacheMode;
import io.yc.library.httpokgo.cookie.CookieJarImpl;
import io.yc.library.httpokgo.https.HttpsUtils;
import io.yc.library.httpokgo.interceptor.HttpLoggingInterceptor;
import io.yc.library.httpokgo.model.HttpHeaders;
import io.yc.library.httpokgo.model.HttpParams;
import io.yc.library.httpokgo.request.DeleteRequest;
import io.yc.library.httpokgo.request.GetRequest;
import io.yc.library.httpokgo.request.HeadRequest;
import io.yc.library.httpokgo.request.OptionsRequest;
import io.yc.library.httpokgo.request.PatchRequest;
import io.yc.library.httpokgo.request.PostRequest;
import io.yc.library.httpokgo.request.PutRequest;
import io.yc.library.httpokgo.request.TraceRequest;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.yc.library.httpokgo.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：2016/1/12
 * 描    述：网络请求的入口类
 * 修订历史：
 * ================================================
 */
public class HttpRequestUtils {
    public static final long DEFAULT_MILLISECONDS = 60000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Application context;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期
    private String mBaseurl;

    public HttpRequestUtils() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.NO_CACHE;


    }

    public static HttpRequestUtils getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        private static HttpRequestUtils holder = new HttpRequestUtils();
    }

    /** get请求 */
    public static  GetRequest  get(String url) {
        return new GetRequest<>(url);
    }

    /** post请求 */
    public static  PostRequest  post(String url) {
        return new PostRequest<>(url);
    }

    /** put请求 */
    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    /** head请求 */
    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    /** delete请求 */
    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    /** options请求 */
    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    /** patch请求 */
    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    /** trace请求 */
    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用 */
    public HttpRequestUtils init(Application app) {
        context = app;


        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("HttpRequestUtils");
        loggingInterceptor.setPrintLevel(HttpUtils.isApkDebugable(getContext())?HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        loggingInterceptor.setColorLevel(Level.INFO);
        System.out.println("HttpUtils.isApkDebugable(getContext()):"+HttpUtils.isApkDebugable(getContext()));
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(HttpRequestUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(HttpRequestUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(HttpRequestUtils.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
        return this;
    }



    public HttpRequestUtils setBaseurl(String mBaseurl) {
        this.mBaseurl = mBaseurl;

        return  this;
    }

    public String getBaseurl() {
        return mBaseurl;
    }

    /** 获取全局上下文 */
    public Context getContext() {
        io.yc.library.httpokgo.utils.HttpUtils.checkNotNull(context, "please call HttpRequestUtils.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        io.yc.library.httpokgo.utils.HttpUtils.checkNotNull(okHttpClient, "please call HttpRequestUtils.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /** 必须设置 */
    public HttpRequestUtils setOkHttpClient(OkHttpClient okHttpClient) {
        io.yc.library.httpokgo.utils.HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /** 获取全局的cookie实例 */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }

    /** 超时重试次数 */
    public HttpRequestUtils setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /** 超时重试次数 */
    public int getRetryCount() {
        return mRetryCount;
    }

    /** 全局的缓存模式 */
    public HttpRequestUtils setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** 获取全局的缓存模式 */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** 全局的缓存过期时间 */
    public HttpRequestUtils setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** 获取全局公共请求参数 */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** 添加全局公共请求参数 */
    public HttpRequestUtils addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /** 获取全局公共请求头 */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** 添加全局公共请求参数 */
    public HttpRequestUtils addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** 根据Tag取消请求 */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 根据Tag取消请求 */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 取消所有请求请求 */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** 取消所有请求请求 */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
