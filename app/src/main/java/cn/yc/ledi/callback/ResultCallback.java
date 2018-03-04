package cn.yc.ledi.callback;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import io.otherutils.tooles.SysLog;
import io.yc.library.httpokgo.callback.JsonCallback;

/**
 * Created by Administrator on 2018-02-20.
 */

public class ResultCallback<T> extends JsonCallback<T> {

    private Dialog mDialog;
    private boolean isShowProgress = true;
    private boolean isCancel = true;
    private String message;
    private Context mContext;

    public ResultCallback() {


    }

    public ResultCallback(Context mContext) {
        this.mContext = mContext;
        this.mDialog = getProgressDialog();
    }

    public Context getContext() {
        return mContext;
    }


    public void setCancel(boolean cancel) {
        isCancel = cancel;
    }


    @Override
    public ResultCallback setDialog(Dialog dialog) {

        super.setDialog(dialog);
        return this;
    }

    /**
     * 生成 Dialog对话框
     *
     * @return
     */
    private Dialog getProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("加载中...");
        progressDialog.setCancelable(isCancel);
        if (isCancel) {
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {

                }
            });
        }
        return progressDialog;
    }

    @Override
    public void onError(int code, String msg) {
        super.onError(code, msg);
    }

    /**
     * 展示进度框
     */
    private void showProgress() {

        if (mDialog != null) {
            mDialog.show();
        }
    }


    /**
     * 取消进度框
     */
    private void dismissProgress() {

        if (mDialog != null) {
            if (mDialog.isShowing()) {
                mDialog.dismiss();
            }
        }
    }

}
