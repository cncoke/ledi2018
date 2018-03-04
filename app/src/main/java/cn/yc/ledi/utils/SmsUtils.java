package cn.yc.ledi.utils;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.listener.PublicReturnListener;

/**
 * Created by Administrator on 2018-02-27.
 */

public class SmsUtils {


    public static void sendCode(String phone, final ResultCallback returnListener) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达

                    returnListener.onFinish();
//                    mCountDownTimerUtils.start();
                } else{
                    // TODO 处理错误的结果
//                    dialogManage.alert("发送失败!");

                    returnListener.onError(result,"发送失败");
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode("86", phone);
    }




    // 提交验证码，其中的code表示验证码，如“1357”
    public static void submitCode(  String phone, String code,final ResultCallback returnListener) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    returnListener.onFinish();
//                    onServerSubmit();
                } else{
                    // TODO 处理错误的结果
//                    dialogManage.alert("验证失败");
                    returnListener.onError(result,"验证失败");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode("86", phone, code);
    }
}
