package cn.yc.ledi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import cn.yc.ledi.R;
import cn.yc.ledi.bean.BaseResultBean;
import cn.yc.ledi.bean.CarBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.utils.PreferencesUtils;
import cn.yc.ledi.utils.PublicUtils;
import cn.yc.ledi.utils.ServerUtils;
import io.yc.library.httpokgo.model.BaseResult;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Fragment_mine extends BaseFragment {
    SwipeRefreshLayout swipeRefreshView;
    View view;
    TextView tv_carname, tv_cardnumber, tv_ordernum, tv_balance, tv_enddate,tv_address;

    ToggleButton tb_switch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, null);
        initView(getActivity());
        initUserInfo();
        return view;
    }

    @Override
    public void initView(Context context) {
        super.initView(context);


        tv_carname = (TextView) view.findViewById(R.id.tv_carname);
        tv_cardnumber = (TextView) view.findViewById(R.id.tv_cardnumber);
        tv_ordernum = (TextView) view.findViewById(R.id.tv_ordernum);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        tv_enddate = (TextView) view.findViewById(R.id.tv_enddate);
        tv_address = (TextView) view.findViewById(R.id.tv_address);
        tb_switch= (ToggleButton) view.findViewById(R.id.tb_switch);
        tb_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                PreferencesUtils.getInstance().setIsPlay(b);
            }
        });
        tb_switch.setChecked(PreferencesUtils.getInstance().getIsPlay());
        swipeRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.id_swipe_ly);
        swipeRefreshView.setProgressBackgroundColorSchemeResource(android.R.color.white);
        swipeRefreshView.setColorSchemeResources(R.color.public_button_normal, R.color.colorPrimary, R.color.colorPrimaryDark);
        swipeRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initUserInfo();
            }
        });

    }


    private void initUserInfo() {
        swipeRefreshView.setRefreshing(false);

        ServerUtils.getInstance().getUserInfo(new ResultCallback<BaseResult<CarBean>>() {
            @Override
            public void onSuccess(BaseResult<CarBean> response) {
                swipeRefreshView.setRefreshing(false);
                settingView(response.getResult());
            }
        });
    }

    public void settingView(final CarBean v) {
//        swipeRefreshView.setRefreshing(true);
        tv_carname.setText(v.getCarname());
        tv_cardnumber.setText(v.getLicense_zh() + v.getLicense_city() + v.getLicense_num());

        tv_ordernum.setText(v.getOrdernum());
        tv_balance.setText(v.getBalance() + "");
        tv_enddate.setText(PublicUtils.formatDate("yyyy-MM-dd", Long.parseLong(v.getEnddate()) * 1000));
    }
}