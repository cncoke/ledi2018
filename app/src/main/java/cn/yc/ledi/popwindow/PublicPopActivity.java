package cn.yc.ledi.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.List;

import cn.yc.ledi.R;
import cn.yc.ledi.adapter.CommonAdapter;
import cn.yc.ledi.bean.ViewHolder;
import cn.yc.ledi.listener.PublicReturnListener;

import static cn.yc.ledi.R.id.gridView1;

/**
 * Created by Administrator on 2017-05-27.
 */

public class PublicPopActivity extends PopupWindow {

    private Context mContext;
    private int layoutId;
    private GridView gridView;
    private List<String> data;
    private PublicReturnListener publicReturnListener;

    public PublicPopActivity(Context context, int layoutid, List<String> data) {
        super(context);

        mContext = context;
        this.layoutId = layoutid;
        this.data
                = data;
        init();
    }

    private void init() {

        View view = LayoutInflater.from(mContext).inflate(layoutId, null);
        setContentView(view);
        gridView = (GridView) view.findViewById(gridView1);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);

        gridView.setAdapter(new CommonAdapter<String>(mContext, data, R.layout.item_text_one) {
            public void convert(ViewHolder holder, String t) {

                holder.setText(R.id.textView, t);
//                holder.setImageURL(R.id.imageView1, t.getImagurl());

            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (publicReturnListener != null) {
                    publicReturnListener.onSuccess(data.get(position));
                }
                dismiss();
            }
        });
    }

    public PublicReturnListener getPublicReturnListener() {
        return publicReturnListener;
    }

    public void setPublicReturnListener(PublicReturnListener publicReturnListener) {
        this.publicReturnListener = publicReturnListener;
    }
}
