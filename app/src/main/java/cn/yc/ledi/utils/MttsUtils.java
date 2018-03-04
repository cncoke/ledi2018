package cn.yc.ledi.utils;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.listener.LocalSynthesizerListener;

/**
 * Created by Administrator on 2017-06-21.
 */

public class MttsUtils {

    private volatile static MttsUtils instance;
    LocalSynthesizerListener mSynListener = new LocalSynthesizerListener();
    SpeechSynthesizer mTts;

    public MttsUtils (){
        mTts = SpeechSynthesizer.createSynthesizer(SysApplication.getInstance().getBaseContext(), null);

        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");// 设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");// 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "100");// 设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); // 设置云端

    }

    public static MttsUtils getInstance() {
        if (instance == null) {
            synchronized (MttsUtils.class) {
                if (instance == null) {
                    instance = new MttsUtils();
                }
            }
        }
        return instance;
    }
    public void playCallInfo(String str) {

        if (PreferencesUtils.getInstance().getIsPlay()) {

            mTts.startSpeaking(str, mSynListener);
        }

    }

}
