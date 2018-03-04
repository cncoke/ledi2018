package cn.yc.ledi.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.yc.ledi.R;
import cn.yc.ledi.widget.TitleBar;
import io.otherutils.dialog.DialogManage;

/**
 * Created by Administrator on 2017/5/24.
 */

public class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    protected DialogManage dialogManage;
    @ViewInject(R.id.title_bar)
    protected TitleBar title_bar;

    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            getHandleMessage(msg);
        }
    };

    protected void getHandleMessage(Message msg) {

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onTitleLeftLayout(View v) {
        finish();
    }

    public void onSubmit(View v) {
    }

    protected void initView(Context mContext) {
        x.view().inject((Activity) mContext);
        this.mContext = mContext;
        this.dialogManage = new DialogManage(mContext);

    }

    protected void setRightText(String txt) {
        if (title_bar != null) {
            title_bar.setRightText(txt);
        }
    }
}
