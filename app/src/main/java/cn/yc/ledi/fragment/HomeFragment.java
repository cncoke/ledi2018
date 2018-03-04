package cn.yc.ledi.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yc.ledi.R;

/**
 * Created by Administrator on 2017-06-10.
 */

public class HomeFragment extends Fragment {

    protected View layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (layout != null) {
            return layout;
        }

        layout = inflater.inflate(R.layout.activity_home, container, false);
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
