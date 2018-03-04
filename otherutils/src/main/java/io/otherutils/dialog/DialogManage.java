package io.otherutils.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ledi.yc.io.otherutils.R;


public class DialogManage {

    public DialogManage mDialogManage;
    private Context mContext;
    private Dialog dialog;
    private static String title = "加载中..";


    public DialogManage() {
        // TODO Auto-generated constructor stub
    }

    public   Dialog getDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext, R.style.loading);
        dialog.setContentView(R.layout.loadinglayout);// 此处布局为一个progressbar
        dialog.setTitle(title);
        dialog.setCancelable(true);
        return dialog;

    }

    public DialogManage(Context cxt) {
        mContext = cxt;
    }

    public void loading() {

        loading("");
    }

    public void toast(String title) {

        Toast.makeText(mContext, title, Toast.LENGTH_LONG).show();
    }

    public void loading(String title) {
        if (dialog == null || !dialog.isShowing()) {

            dialog=getDialog(mContext);
            if (title != null && !title.equals("")) {
                TextView textView = (TextView) dialog.findViewById(R.id.textView1);
                textView.setVisibility(View.VISIBLE);
                textView.setText(title);
            }
            dialog.show();
        }
    }

    String text = "";
    TextView tv_button;

    public void inputText(String defaultString, final OnClickReturnStringListener clickListener) {
        inputText(defaultString, defaultString, InputType.TYPE_CLASS_TEXT, clickListener);
    }

    public void inputText(String defaultString, String hintString, int inputType,
                          final OnClickReturnStringListener clickListener) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_input_layout, null);
        tv_button = (TextView) view.findViewById(R.id.tv_button);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        EditText dialog_message = (EditText) view.findViewById(R.id.et_text);
        dialog_message.setText(defaultString);
        dialog_message.setInputType(inputType);
        dialog_message.setHint(hintString);

        dialog_message.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

                text = s.toString();
                tv_button.setOnClickListener(onSubmitClickListener(clickListener, text));
            }
        });
        if (dialog != null && dialog.isShowing()) {
            cancel();
        }

        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        tv_cancel.setOnClickListener(onCancelClickListener);
        dialog.show();
    }

    public interface OnClickReturnStringListener {
        void onSubmit(String text);
    }

    private OnClickListener onSubmitClickListener(final OnClickReturnStringListener clickListener, final String text) {
        OnClickListener onSubmitClickListener = new OnClickListener() {

            @Override
            public void onClick(View v) {

                cancel();
                clickListener.onSubmit(text);
                // clickListener.onClick(v);

            }
        };
        return onSubmitClickListener;
    }

    public void confirm(String message, final OnClickListener clickListener) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_confirm_layout, null);
        TextView tv_button = (TextView) view.findViewById(R.id.tv_button);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        TextView dialog_message = (TextView) view.findViewById(R.id.dialog_message);
        if (dialog != null && dialog.isShowing()) {
            cancel();
        }

        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        dialog_message.setText(message);
        tv_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                cancel();
                clickListener.onClick(v);
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onCancelClickListener.onClick(v);
            }
        });
        dialog.show();

    }

    public void alert(String message) {

        alert(message, null);
    }

    public void alert(String message, final OnClickListener clickListener) {

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_alert_layout, null);
        TextView tv_button = (TextView) view.findViewById(R.id.tv_button);
        TextView dialog_message = (TextView) view.findViewById(R.id.dialog_message);
        if (dialog != null && dialog.isShowing()) {
            cancel();
        }

        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        dialog_message.setText(message);
        tv_button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
                if (clickListener != null) {
                    clickListener.onClick(v);
                } else {
                    onCancelClickListener.onClick(v);
                }
            }
        });

        dialog.show();

    }

    public void cancel() {
        // TODO Auto-generated method stub
        if (dialog != null) {
            dialog.cancel();
        }
    }

    OnClickListener onCancelClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            cancel();

        }
    };

}
