package cn.yc.ledi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.otherutils.dialog.DialogManage;


/**
 * Created by Administrator on 2017-06-22.
 */
public class BaseFragment extends Fragment {
    protected DialogManage dialogManage;
    protected Context mContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void initView(Context context){
        mContext=context;
        this.dialogManage = new DialogManage(context);

    }
}
