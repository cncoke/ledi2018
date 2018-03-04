package cn.yc.ledi.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.HashMap;
import java.util.Map;

import cn.yc.ledi.R;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.utils.ServerUtils;


@ContentView(R.layout.activity_updatepassword)
public class UpdatePasswordActivity extends BaseActivity {


    @ViewInject(R.id.et_password1)
    EditText et_password1;
    @ViewInject(R.id.et_password2)
    EditText et_password2;
    @ViewInject(R.id.tv_userphone)
    TextView tv_userphone;

    String password1, password2, token, userphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        token = getIntent().getStringExtra("token");
        userphone = getIntent().getStringExtra("userphone");
        SysApplication.getInstance().addActivity(this);
        initView(this);
    }

    @Override
    protected void initView(Context mContext) {
        super.initView(mContext);
        tv_userphone.setText(userphone);
    }

    protected void onServerSubmit() {
//
        dialogManage.loading();
//
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("password", password1);

        ServerUtils.getInstance().updatePassword(map, new ResultCallback<CarBean>() {
            @Override
            public void onSuccess(CarBean response) {
                dialogManage.confirm("密码修改成功 请牢记", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }

            @Override
            public void onError(int code,String msg) {
                super.onError(code,msg);
                dialogManage.alert(msg);
            }
        });

    }

    private void gotoActivity(CarBean carBean) {


    }
//    Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 1) {
//                String error = (String) msg.obj;
//                dialogManage.alert(error);
//            }
//        }
//    };


    public void onLogin(View v) {

        password1 = et_password1.getText().toString();
        password2 = et_password2.getText().toString();
        if (TextUtils.isEmpty(password1)) {
            dialogManage.alert("新密码不能空");
        } else if (TextUtils.isEmpty(password2)) {
            dialogManage.alert("确认密码不能空");
        } else if (!password2.equals(password1)) {
            dialogManage.alert("两次密码不一致");
        } else if (TextUtils.isEmpty(token)) {
            dialogManage.alert("token参数错误,请重新试一次");
        } else {
            onServerSubmit();

        }

    }
}
