package cn.yc.ledi.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yc.ledi.R;
import cn.yc.ledi.bean.OrderBean;
import cn.yc.ledi.utils.PublicUtils;
import io.otherutils.dialog.DialogManage;

/**
 * Created by Administrator on 2017-06-23.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    public List<OrderBean> datas = null;
    public Context mContext;
    private DialogManage dialogManage;

    public OrderListAdapter(Context context, List<OrderBean> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_order_info, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final OrderBean data = datas.get(position);
        viewHolder.tv_name.setText(data.getUsername());
        viewHolder.tv_sta_name.setText(PublicUtils.formatStartPoiName(data.getStartpoiname()));
        viewHolder.tv_end_name.setText(PublicUtils.formatEndPoiName(data.getEndpoiname()));

        viewHolder.tv_inputtime.setText(PublicUtils.formatDate("MM月dd日hh时mm分", data.getInputtime() * 1000));
        Integer state = Integer.parseInt(data.getState());
//        viewHolder.tv_button.setText(PublicUtils.getOrderState(state));
        if (data != null && state == 1) {


//            viewHolder.iv_phone.setVisibility(View.VISIBLE);
//            viewHolder.tv_button.setBackgroundResource(R.drawable.selector_public_button_background);
//            viewHolder.tv_button.setTextColor(mContext.getResources().getColorStateList(R.color.common_white));
            viewHolder.tv_button.setVisibility(View.VISIBLE);
            viewHolder.tv_button.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub


                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + data.getUserphone()));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mContext.startActivity(intent);
//					if (gotoOrder != null) {
////						gotoOrder.gotoPhone(subdata.getPassengerphone());
//					}
                }
            });
//            viewHolder.tv_button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Map<String,String> map = new HashMap<String, String>();
//                    map.put("type","1");
//                    map.put("position",position+"");
//                    map.put("order_id",data.getOrder_id()+"");
//                    adapterListener.onSuccess(map);
//
//                }
//            });
        } else {
            viewHolder.tv_button.setVisibility(View.GONE);
//            viewHolder.iv_phone.setVisibility(View.GONE);
//            viewHolder.tv_name.setText(data.getUsername());
//            viewHolder.tv_button.setBackgroundResource(R.color.common_white);
//            viewHolder.tv_button.setTextColor(mContext.getResources().getColorStateList(R.color.public_button_pressed));

        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_sta_name, tv_end_name, tv_distance, tv_button,tv_inputtime,
                tv_name;
        public ImageView iv_phone;

        public ViewHolder(View view) {
            super(view);
            iv_phone = (ImageView) view.findViewById(R.id.iv_phone);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_sta_name = (TextView) view.findViewById(R.id.tv_sta_name);
            tv_end_name = (TextView) view.findViewById(R.id.tv_end_name);
            tv_button = (TextView) view.findViewById(R.id.tv_button);
            tv_distance = (TextView) view.findViewById(R.id.tv_distance);
            tv_inputtime = (TextView) view.findViewById(R.id.tv_inputtime);
        }
    }


/*

    private PublicReturnListener adapterListener = new PublicReturnListener() {
        @Override
        public void onSuccess(Object data) {
            final Map<String, String> map = (Map<String, String>) data;
            String type = map.get("type").toString();

            if (type.equals("1")) {
                dialogManage.loading();
                String order_id = map.get("order_id").toString();
                ServerUtils.getInstance().setOrderState(order_id, 2, new ServerUtils.OnResult<BaseResultBean>() {
                    @Override
                    public void OnSuccess( BaseResultBean  cls) {
                        datas.get(Integer.parseInt(map.get("position").toString())).setState(2+"");
                        DataUtils.getInstance().getWaiteSuccessOrder().remove(cls);
                        dialogManage.cancel();
                        notifyDataSetChanged();
                    }

                    @Override
                    public void OnError(String msg) {
                        dialogManage.alert(msg);

                    }
                });
            }
        }
    };
    */
}
