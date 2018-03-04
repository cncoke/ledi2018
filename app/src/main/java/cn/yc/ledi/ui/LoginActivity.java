package cn.yc.ledi.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yc.ledi.R;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.fragment.FragmentMainActivity;
import cn.yc.ledi.utils.DataUtils;
import cn.yc.ledi.utils.PreferencesUtils;
import cn.yc.ledi.utils.ServerUtils;
import io.yc.library.httpokgo.model.BaseResult;


@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.et_mobilephone)
    EditText et_mobilephone;
    @ViewInject(R.id.et_password)
    EditText et_password;
    private static final int BAIDU_READ_PHONE_STATE = 100;
    String mobilephone = "", password = "";
    String car_id = "";

    boolean checkPermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView(this);

        setRightText("注册");
        AndPermission.with(this)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {

                        checkPermission = true;
                        autoLogin();
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        checkPermission = false;
                        dialogManage.alert("权限不足");
                    }
                }).start();
    }

    private void autoLogin() {

        try {
            car_id = PreferencesUtils.getInstance().getCar_id();
            CarBean car_bean = DataUtils.getInstance().getDb().findById(CarBean.class, car_id);

            System.out.println("car_bean:" + car_bean);
            if (car_bean != null) {
                mobilephone = car_bean.getMobilephone();
                password = car_bean.getPassword();
                et_mobilephone.setText(car_bean.getMobilephone());
            }

            if (PreferencesUtils.getInstance().getIsLogin()) {
                onServerSubmit();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public void onTitleRightLayout(View v) {
        Intent intent = new Intent();
        intent.setClass(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    protected void onServerSubmit() {


        if (checkPermission) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("mobilephone", mobilephone);
            map.put("password", password);
            map.put("phonetag", SysApplication.getInstance().getPhoneTag());
            ServerUtils.getInstance().login(map, new ResultCallback<BaseResult<CarBean>>() {
                @Override
                public void onSuccess(BaseResult<CarBean> response) {
                    super.onSuccess(response);
                    PreferencesUtils.getInstance().setCar_id(response.getResult().getCar_id());
                    PreferencesUtils.getInstance().setCurrentCarInfo(response.getResult());
                    PreferencesUtils.getInstance().setIsLogin(true);
                    try {
                        DataUtils.getInstance().getDb().saveOrUpdate(response.getResult());
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                    gotoMain();
                }

                @Override
                public void onError(int code, String msg) {
                    super.onError(code, msg);
                    dialogManage.alert(msg);
                }
            });
        } else {
            dialogManage.alert("手机软件权限未开启");
        }
    }

    private void gotoMain() {

        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
//        Intent intent = new Intent(LoginActivity.this, PayActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLogin(View v) {

        mobilephone = et_mobilephone.getText().toString();
        password = et_password.getText().toString();
        if (TextUtils.isEmpty(mobilephone)) {
            dialogManage.alert("电话号码不能空");
        } else if (TextUtils.isEmpty(password)) {
            dialogManage.alert("密码不能空");
        } else {
            onServerSubmit();
        }

    }

}
