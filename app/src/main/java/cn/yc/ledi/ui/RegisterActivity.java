package cn.yc.ledi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.yc.ledi.R;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.base.Config;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.BaseResultBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.listener.PublicReturnListener;
import cn.yc.ledi.popwindow.PublicPopActivity;
import cn.yc.ledi.utils.BaiduLocationUtils;
import cn.yc.ledi.utils.CountDownTimerUtils;
import cn.yc.ledi.utils.ServerUtils;
import cn.yc.ledi.utils.SmsUtils;
import io.otherutils.tooles.IdcardValidator;

@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {

    @ViewInject(R.id.et_carphone)
    EditText et_carphone;
    @ViewInject(R.id.et_sendcode)
    TextView et_sendcode;
    @ViewInject(R.id.tv_license_city)
    TextView tv_license_city;
    @ViewInject(R.id.tv_sendcode)
    TextView tv_sendcode;
    @ViewInject(R.id.tv_currentCity)
    TextView tv_currentCity;

    @ViewInject(R.id.et_carname)
    TextView et_carname;

    @ViewInject(R.id.et_license_num)
    TextView et_license_num;
    @ViewInject(R.id.et_password)
    TextView et_password;

    @ViewInject(R.id.et_cardnumber)
    EditText et_cardnumber;

    EventHandler eh;
    CountDownTimerUtils mCountDownTimerUtils;

    String license_zh = "黑";
    String license_city = "B";
    String carphone = "", carname = "", license_num = "", sendcode = "", password = "", cardnumber = "";
    IdcardValidator idcardValidator;
    String province = "", city = "", district = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        initView(this);

        mCountDownTimerUtils = new CountDownTimerUtils(tv_sendcode, 60000, 1000);
        idcardValidator = new IdcardValidator();

        BaiduLocationUtils.getInstance().getLocation(this, new PublicReturnListener() {
            @Override
            public void onSuccess(Object data) {
                BDLocation location = (BDLocation) data;
                if(!TextUtils.isEmpty(location.getProvince())&&!TextUtils.isEmpty(location.getCity())&&!TextUtils.isEmpty(location.getDistrict())) {
                    province = location.getProvince();
                    city = location.getCity();
                    district = location.getDistrict();

                    mHandler.sendEmptyMessage(1000);

                }

            }
        });

    }

    @Override
    protected void getHandleMessage(Message msg) {
        if (msg.what == 1000) {
            tv_currentCity.setText(city + district);
        }
    }

    public void openPopZh(View v) {


        List<String> city = Arrays.asList(Config.cityen);
        PublicPopActivity publicPopActivity = new PublicPopActivity(this, R.layout.public_gridview, city);
        publicPopActivity.showAsDropDown(v);
        publicPopActivity.setOutsideTouchable(true);
        publicPopActivity.setFocusable(true);
        publicPopActivity.setPublicReturnListener(new PublicReturnListener() {
            @Override
            public void onSuccess(Object data) {
                setCityText(data.toString());
            }
        });
    }

    private void setCityText(String s) {
        license_city = s;
        tv_license_city.setText(s);
    }


    public void sendSmsCode(View view) {
        String s = et_carphone.getText().toString();
       // SMSSDK.getVerificationCode("+86", s);
//        sendCode("86",s);

        SmsUtils.sendCode(s,new ResultCallback<String>(){
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


    protected void onServerSubmit() {

        dialogManage.loading();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobilephone", carphone);
        map.put("carname", carname);
        map.put("password", password);
        map.put("license_zh", license_zh);
        map.put("license_city", license_city);
        map.put("license_num", license_num);
        map.put("cardnumber", cardnumber);
        map.put("cartype", "0");
        map.put("phonetag", "0");
        map.put("province", province);
        map.put("city", city);
        map.put("district", district);

        ServerUtils.getInstance().register(map, new ResultCallback<BaseResultBean<String>>() {
            @Override
            public void onSuccess(BaseResultBean<String> response) {
                dialogManage.toast("注册成功!");
                Intent intent = new Intent();
                intent.setClass(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onSubmit(View v) {
        super.onSubmit(v);

        carphone = et_carphone.getText().toString();
        carname = et_carname.getText().toString();
        license_num = et_license_num.getText().toString();
        sendcode = et_sendcode.getText().toString();
        password = et_password.getText().toString();
        cardnumber = et_cardnumber.getText().toString();
        if (TextUtils.isEmpty(province) || TextUtils.isEmpty(city) || TextUtils.isEmpty(district)) {
            dialogManage.alert("定位尚未成功，请检查权限或GPS");
        }
        else if (TextUtils.isEmpty(carphone)) {
            dialogManage.alert("电话号码不能空");

        } else if (TextUtils.isEmpty(carname)) {
            dialogManage.alert("司机姓名不能空");
        } else {
            if (TextUtils.isEmpty(cardnumber) || !idcardValidator.IDCardValidate(cardnumber).equals("")) {
                dialogManage.alert("司机身份证不正确");
            } else if (license_num.length() != 5) {
                dialogManage.alert("请录入正确车牌");
            } else if (TextUtils.isEmpty(sendcode)) {
                dialogManage.alert("请录入验证码");
            } else if (TextUtils.isEmpty(password)) {
                dialogManage.alert("请录入登录密码");
            } else {
                if (sendcode.equals("0000")) {
                    onServerSubmit();
                } else {


                    //SMSSDK.submitVerificationCode("+86", carphone, sendcode);
//                    submitCode("86",carphone,sendcode);
                    SmsUtils.submitCode(carphone,sendcode,new ResultCallback<String>(){
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



/*
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达

                    mCountDownTimerUtils.start();
                } else{
                    // TODO 处理错误的结果
                    dialogManage.alert("发送失败!");
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果

                    onServerSubmit();
                } else{
                    / / TODO 处理错 误的结果
                    dialogManage.alert("验证失败");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }*/
}
