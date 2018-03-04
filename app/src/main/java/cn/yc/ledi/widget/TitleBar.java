package cn.yc.ledi.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.yc.ledi.R;


/**
 * Created by Administrator on 2017/5/24.
 */

public class TitleBar extends RelativeLayout {
    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init(context, attrs);
    }

    LinearLayout title_bar_left_layout;
    LinearLayout title_bar_right_layout;
    TextView title_bar_text;
    TextView title_bar_right_text;
    ImageView title_bar_right_image;

    private void init(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        LayoutInflater.from(context).inflate(R.layout.common_titlebar, this);
        title_bar_left_layout = (LinearLayout) findViewById(R.id.title_bar_left_layout);
        title_bar_right_layout = (LinearLayout) findViewById(R.id.title_bar_right_layout);
        title_bar_text = (TextView) findViewById(R.id.title_bar_text);
        title_bar_right_text = (TextView) findViewById(R.id.title_bar_right_text);
        title_bar_right_image = (ImageView) findViewById(R.id.title_bar_right_image);

        parseStyle(context, attrs);
    }

    private void parseStyle(Context context, AttributeSet attrs) {
        // TODO Auto-generated method stub
        if (attrs != null) {

            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
            String title = ta.getString(R.styleable.TitleBar_titleBarTitle);
            title_bar_text.setText(title);

            Drawable rightDrawable = ta.getDrawable(R.styleable.TitleBar_titleBarRightImage);
            if (null != rightDrawable) {
                title_bar_right_image.setVisibility(View.VISIBLE);
                title_bar_right_image.setImageDrawable(rightDrawable);
            } else {
                title_bar_right_image.setVisibility(View.GONE);
            }

            String rightText = ta.getString(R.styleable.TitleBar_titleBarRightText);
            if (null != rightText) {
                title_bar_right_text.setText(rightText);
                title_bar_right_text.setVisibility(View.VISIBLE);
            } else {
                title_bar_right_text.setVisibility(View.GONE);
            }

            ta.recycle();
        }
    }

    public void setTitle_bar_text(String title) {
        // System.out.println(title);
        title_bar_text.setText(title);
    }

    public void setLeftLayoutClickListener(OnClickListener listener) {
        title_bar_left_layout.setOnClickListener(listener);
    }

    public void setRightLayoutClickListener(OnClickListener listener) {
        title_bar_right_layout.setOnClickListener(listener);
    }

    public void setRightImageDrawable(int drawable) {
        title_bar_right_image.setVisibility(View.VISIBLE);
        title_bar_right_image.setImageResource(drawable);
    }

    public void setRightText(String rightText) {
        if (null != rightText) {
            title_bar_right_text.setText(rightText);
            title_bar_right_text.setVisibility(View.VISIBLE);
        } else {
            title_bar_right_text.setVisibility(View.GONE);
        }
    }
}
