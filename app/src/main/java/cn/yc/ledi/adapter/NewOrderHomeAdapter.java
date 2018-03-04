package cn.yc.ledi.adapter;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by Administrator on 2017-06-23.
 */
public class NewOrderHomeAdapter extends BaseAdapter {


    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
/*
*
* public class NewOrderHomeAdapter extends RecyclerView.Adapter<NewOrderHomeAdapter.ViewHolder> {
    public List<OrderBean> datas = new ArrayList<OrderBean>();
    public Context mContext;

    public NewOrderHomeAdapter(Context context ) {
        this.mContext = context;
    }

    public void setDatas(List<OrderBean> data) {
        datas.clear();
        datas.addAll(data);
        for (OrderBean o : datas
             ) {

            System.out.println("order id :"+o.getOrder_id()+" "+o.getStartpoiname());
        }
        notifyDataSetChanged();
    }


    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home_news_order, viewGroup, false);
        return new ViewHolder(view);
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final OrderBean data = datas.get(position);
        System.out.println("onBindViewHolder:"+data.getOrder_id() + " "+data.getStartpoiname() );

        viewHolder.tv_startpoiname.setText(PublicUtils.formatStartPoiName(data.getStartpoiname()));
        viewHolder.tv_endpoiname.setText(PublicUtils.formatEndPoiName(data.getEndpoiname()));

        Integer state = Integer.parseInt(data.getState());
        if (data != null && state == 1) {

            viewHolder.tv_submitOrder.setVisibility(View.VISIBLE);
            viewHolder.tv_submitOrder.setOnClickListener(new View.OnClickListener() {

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
                }
            });
        } else {
            viewHolder.tv_submitOrder.setVisibility(View.GONE);

        }
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }


    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_startpoiname, tv_endpoiname, tv_submitOrder;
        public ImageView iv_phone;

        public ViewHolder(View view) {
            super(view);
            tv_startpoiname = (TextView) view.findViewById(R.id.tv_startpoiname);
            tv_endpoiname = (TextView) view.findViewById(R.id.tv_endpoiname);
            tv_submitOrder = (TextView) view.findViewById(R.id.tv_submitOrder);
        }
    }



//    notifyDataSetChanged

}
*/