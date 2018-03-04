package cn.yc.ledi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.yc.ledi.R;
import cn.yc.ledi.adapter.GoldRecordAdapter;
import cn.yc.ledi.bean.GoldRecordBean;
import cn.yc.ledi.callback.ResultCallback;
import cn.yc.ledi.utils.ServerUtils;
import cn.yc.ledi.widget.TitleBar;

//import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * Created by Administrator on 2017-06-22.
 */

public class Fragment_order extends BaseFragment {
    View view;
    int pageNum = 1;
    List<GoldRecordBean> list = new ArrayList<GoldRecordBean>();

    private XRecyclerView mRecyclerView;
    private GoldRecordAdapter mAdapter;
    int type = 100;
    boolean isShowTitleBar=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order, null);

        System.out.println("--------onCreateView--------");
        if(!isShowTitleBar) {
            TitleBar title_bar = view.findViewById(R.id.title_bar);
            title_bar.setVisibility(View.GONE);
        }

        initView(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        System.out.println("--------onStart--"+type+"------");

        initData();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("--------onCreate--------");
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", 100);
            isShowTitleBar=false;
        }

    }


    public static Fragment_order newInstance(int type) {
        Fragment_order myFragment = new Fragment_order();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        System.out.println("----------------------data---------------------");
        System.out.println(type);
        System.out.println("----------------------data---------------------");
        myFragment.setArguments(bundle);

        return myFragment;
    }

    @Override
    public void initView(Context context) {
        super.initView(context);

//        dialogManage.loading();

        mRecyclerView = (XRecyclerView) view.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallBeat);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
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


                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pageNum++;
                        initData();
                    }

                }, 1000);

            }
        });

        mAdapter = new GoldRecordAdapter(getActivity(), list);
        mRecyclerView.setAdapter(mAdapter);



    }


    private void initData() {

//        mRecyclerView.refreshComplete();
//        mRecyclerView.refresh();
        list.clear();
        mAdapter.notifyDataSetChanged();
        ServerUtils.getInstance().getGoldRecord(pageNum, type, new ResultCallback<List<GoldRecordBean>>()

        {

            @Override
            public void onSuccess(List<GoldRecordBean> response) {
                if (pageNum == 1) {
                    list.clear();
                }
                for (GoldRecordBean g : response
                        ) {
                    System.out.println("gggggggg:" + g);

                }
                list.addAll(response);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.refreshComplete();
            }
        });

        /*ServerUtils.getInstance().getOrderList(state, pageNum, new ResultCallback<List<OrderBean>>() {

            @Override
            public void onSuccess(List<OrderBean> response) {

                dialogManage.cancel();
                if (pageNum == 1) {
                    list.clear();
                }
                list.addAll(response);
                mAdapter.notifyDataSetChanged();
            }
        });*/
    }
}