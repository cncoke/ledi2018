package cn.yc.ledi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import cn.yc.ledi.R;
import cn.yc.ledi.bean.GoldRecordBean;
import cn.yc.ledi.utils.PublicUtils;


public class GoldRecordAdapter extends RecyclerView.Adapter<GoldRecordAdapter.ViewHolder> {
    public List<GoldRecordBean> datas = null;
    public Context mContext;
    DecimalFormat df = new DecimalFormat("#0.00");

    public GoldRecordAdapter(Context context, List<GoldRecordBean> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public GoldRecordAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_gold_record, viewGroup, false);
        return new GoldRecordAdapter.ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(GoldRecordAdapter.ViewHolder viewHolder, final int position) {
        final GoldRecordBean data = datas.get(position);

        viewHolder.tv_name.setText(data.getPayname());
        viewHolder.tv_order_sn.setText("订单号: " + data.getOrder_sn());
        viewHolder.tv_time.setText(PublicUtils.formatDate("yyyy-MM-dd hh:mm:ss", data.getInputtime() * 1000));
        viewHolder.tv_num.setText(df.format(data.getNum()) + "");
        if (data.getNum() > 0) {
            viewHolder.tv_num.setTextColor(mContext.getResources().getColorStateList(R.color.common_f53e36));
        } else {
            viewHolder.tv_num.setTextColor(mContext.getResources().getColorStateList(R.color.public_button_pressed));

        }


    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time, tv_num,
                tv_name, tv_order_sn;

        public ViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_num = (TextView) view.findViewById(R.id.tv_num);
            tv_order_sn = (TextView) view.findViewById(R.id.tv_order_sn);
        }
    }


}