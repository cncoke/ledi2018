package cn.yc.ledi.listener;

import android.os.Bundle;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

/**
 * Created by Administrator on 2017-06-21.
 */

public class LocalSynthesizerListener implements SynthesizerListener {

    public interface BufferProgressListener{
        void loadSuccess();
    }

    private BufferProgressListener mBufferProgressListener;


    public BufferProgressListener getmBufferProgressListener() {
        return mBufferProgressListener;
    }

    public void setmBufferProgressListener(BufferProgressListener mBufferProgressListener) {
        this.mBufferProgressListener = mBufferProgressListener;
    }

    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        // TODO Auto-generated method stub

        if (arg0==100&&mBufferProgressListener!=null) {
            mBufferProgressListener.loadSuccess();
        }

    }

    @Override
    public void onCompleted(SpeechError arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakBegin() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakPaused() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakResumed() {
        // TODO Auto-generated method stub

    }

}

