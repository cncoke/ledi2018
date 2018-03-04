package cn.yc.ledi.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ViewInject;

import cn.yc.ledi.R;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.base.Config;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.widget.TitleBar;

/**
 * Created by Administrator on 2017-06-20.
 */

public class PublicWebView extends BaseActivity {

    WebView webView1;

    @ViewInject(R.id.title_bar)
    TitleBar title_bar;


    String content;
    String url;
    String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_webview);
        SysApplication.getInstance().addActivity(this);
        initView(this);

    }

    @Override
    protected void initView(Context mContext) {
        super.initView(mContext);

        dialogManage.loading();
        content = getIntent().getStringExtra(Config.STRING_CONTENT);
        url = getIntent().getStringExtra(Config.STRING_URL);
        title = getIntent().getStringExtra(Config.STRING_TITLE);
        webView1= (WebView) findViewById(R.id.webView1);

        if (!TextUtils.isEmpty(title)) {
            title_bar.setTitle_bar_text(title);
        }
        if (!TextUtils.isEmpty(url)) {
            webView1.loadUrl(url);
        }

        if (!TextUtils.isEmpty(content)) {
            webView1.loadDataWithBaseURL("", "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=0.5, maximum-scale=2.0, user-scalable=yes\" /> " + content, "text/html", "UTF-8", "");
        }


        webView1.setWebViewClient(new Callback());
        webView1.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress > 90) {
                    dialogManage.cancel();
                }
            }
        });
        webView1.getSettings().setDefaultTextEncodingName("UTF-8");

        webView1.getSettings().setJavaScriptEnabled(true);

        webView1.getSettings().setSupportZoom(true);
        webView1.getSettings().setBuiltInZoomControls(true);
        webView1.getSettings().setUseWideViewPort(true);
        webView1.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView1.getSettings().setLoadWithOverviewMode(true);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView1.canGoBack()) {
            webView1.goBack();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }


    }
}
