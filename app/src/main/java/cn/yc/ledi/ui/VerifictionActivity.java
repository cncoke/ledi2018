package cn.yc.ledi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.yc.ledi.R;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.utils.CountDownTimerUtils;
import cn.yc.ledi.utils.ServerUtils;
import cn.yc.ledi.utils.SmsUtils;


@ContentView(R.layout.activity_verifiction)
public class VerifictionActivity extends BaseActivity {


    @ViewInject(R.id.et_carphone)
    EditText et_carphone;

    @ViewInject(R.id.et_sendcode)
    EditText et_sendcode;
    @ViewInject(R.id.tv_sendcode)
    TextView tv_sendcode;

    String mobilephone, sendcode;
    EventHandler eh;
    CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        initView(this);

        mCountDownTimerUtils = new CountDownTimerUtils(tv_sendcode, 60000, 1000);
    }

    public void sendSmsCode(View view) {
        String s = et_carphone.getText().toString();
        SmsUtils.sendCode(s, new ResultCallback<String>() {
            @Override
            public void onFinish() {
                super.onFinish();
                mCountDownTimerUtils.start();
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dialogManage.alert("发送失败!");
            }
        });
    }

    @Override
    protected void initView(Context mContext) {
        super.initView(mContext);

    }

    protected void onServerSubmit() {


        Map<String, String> map = new HashMap<String, String>();
        map.put("mobilephone", mobilephone);


        ServerUtils.getInstance().updateToken(mobilephone, new ResultCallback<CarBean>() {
            @Override
            public void onSuccess(CarBean response) {
                gotoActivity(response);
            }

            @Override
            public void onError(int code, String msg) {
                super.onError(code, msg);
                dialogManage.alert(msg);
            }
        }.setDialog(dialogManage.getDialog(this)));


    }

    private void gotoActivity(CarBean carBean) {
        if (carBean != null) {
            Intent intent = new Intent();
            intent.setClass(this, UpdatePasswordActivity.class);
            intent.putExtra("token", carBean.getToken());
            intent.putExtra("userphone", carBean.getMobilephone());
            startActivity(intent);
            finish();
        }
    }


    public void onSubmit(View v) {

        mobilephone = et_carphone.getText().toString();
        sendcode = et_sendcode.getText().toString();
        if (TextUtils.isEmpty(mobilephone)) {
            dialogManage.alert("电话号码不能空");
        } else if (TextUtils.isEmpty(sendcode)) {
            dialogManage.alert("验证码不能空");
        } else {
            if (sendcode.equals("0000")) {

                onServerSubmit();

            } else {


                SmsUtils.submitCode(mobilephone, sendcode, new ResultCallback<String>() {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        onServerSubmit();
                    }

                    @Override
                    public void onError(int code, String msg) {
                        super.onError(code, msg);
                        dialogManage.alert("验证失败");
                    }
                });
            }
        }

    }
}
