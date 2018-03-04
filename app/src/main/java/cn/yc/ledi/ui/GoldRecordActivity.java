package cn.yc.ledi.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.yc.ledi.R;
import cn.yc.ledi.adapter.GoldRecordAdapter;
import cn.yc.ledi.base.BaseActivity;
import cn.yc.ledi.base.SysApplication;
import cn.yc.ledi.bean.GoldRecordBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.utils.ServerUtils;
import cn.yc.ledi.widget.TitleBar;

//import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2017-06-26.
 */

@ContentView(R.layout.activity_order)
public class GoldRecordActivity extends BaseActivity {

    @ViewInject(R.id.title_bar)
    TitleBar title_bar;
    @ViewInject(R.id.recyclerview)
    XRecyclerView mRecyclerView;
    int pageNum = 1;

    List<GoldRecordBean> list = new ArrayList<GoldRecordBean>();
    GoldRecordAdapter mAdapter;

    @ViewInject(R.id.tab_layout)
    TabLayout tab_layout;
    String[] tabs_title = new String[]{"充值", "支出", "收入"};
    int[] tabs_request_code = new int[]{100, 101, 102};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        initView(this);
        initData();
    }


    @Override
    protected void initView(Context mContext) {
        super.initView(mContext);


        title_bar.setTitle_bar_text("交易记录");
        title_bar.setRightText("充值");
        title_bar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GoldRecordActivity.this, PayActivity.class);
                startActivityForResult(intent, 1000);
            }
        });


        for (int i = 0; i < tabs_title.length; i++) {
            tab_layout.addTab(tab_layout.newTab().setText(tabs_title[i]));

        }

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(type!=tabs_request_code[tab.getPosition()]) {
                    type = tabs_request_code[tab.getPosition()];
                    initData();
                }

                System.out.println("-----------onTabSelected-----------");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("-----------onTabReselected-----------");

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                System.out.println("-----------onRefresh-----------");
                // list.clear();
                new Handler().postDelayed(new Runnable() {
                    public void run() {

                        pageNum = 1;
                        initData();
                    }

                }, 1000);


            }

            @Override
            public void onLoadMore() {

                System.out.println("-----------onLoadMore-----------");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pageNum++;
                        initData();
                    }

                }, 1000);

            }
        });

        mAdapter = new GoldRecordAdapter(this, list);
        mRecyclerView.setAdapter(mAdapter);



/*
        String[] tabs_title = new String[]{"充值", "收入", "支出"};
        fragments = new ArrayList<>();
        List<String> tabs = new ArrayList<>();

        for (int i = 0; i < tabs_title.length; i++) {

            tabs.add(tabs_title[i]);

            fragments.add(Fragment_order.newInstance(100 + i));
        }

        ViewPager viewPager = ((ViewPager) findViewById(R.id.view_pager));
        viewPager.setAdapter(new GoldFragmentPagerAdapter(getSupportFragmentManager(), fragments, tabs));
        tab_layout.setupWithViewPager(viewPager);
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("tab:"+tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == RESULT_OK) {
//            recyclerview.refresh();
        }
    }

    int type = 100;

    private void initData() {


        ServerUtils.getInstance().getGoldRecord(pageNum, type, new ResultCallback<List<GoldRecordBean>>()

        {

            @Override
            public void onSuccess(List<GoldRecordBean> response) {
                if (pageNum == 1) {
                    list.clear();
                }

                list.addAll(response);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }
        });
    }
}
