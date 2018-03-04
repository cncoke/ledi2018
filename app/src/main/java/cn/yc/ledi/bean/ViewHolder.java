package cn.yc.ledi.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2017-05-27.
 */

public class ViewHolder {

    private SparseArray<View> mViews;
    private int mPositon;
    private View mConvertView;

    public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPositon = position;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);

        mConvertView.setTag(this);

    }

    public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutid, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutid, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPositon = position;
            return holder;
        }
    }

    public int getPositon() {
        return mPositon;
    }

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getmConvertView() {
        return mConvertView;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;

    }


    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;

    }


    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;

    }


    public ViewHolder setImageURL(int viewId, String url) {
        ImageView view = getView(viewId);
//        ImageLoader.getInstance().displayImage(url, view);
        return this;

    }
}
