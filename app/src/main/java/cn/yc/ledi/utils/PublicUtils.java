package cn.yc.ledi.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Administrator on 2017-06-10.
 */

public class PublicUtils {
    public static String encrypt(String info) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++) {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                } else {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static <T> T resultToBean(JSONObject json, Class<T> clazz) {
        T value = null;
        value = (T) JSON.parseObject(json.getJSONObject("result").toString(), clazz);

        return value;
    }


    public static String formatDate(String format, long timeStamp) {
        String result = "";
        SimpleDateFormat sdr = new SimpleDateFormat(format);
        result = sdr.format(new Date(timeStamp));
        return result;

    }

    public static String formatDistance(String distance) {

        String distance_str = "约" + distance + "米";
        if (TextUtils.isEmpty(distance)) {
            distance_str = "附近10米左右";
        }
        return distance_str;
    }

    public static String formatStartPoiName(String startpoiname) {

        return "从" + startpoiname + "打车";
    }

    public static String formatEndPoiName(String endpoiname) {
        String poi = endpoiname;
        if (TextUtils.isEmpty(poi)) {

            poi = "到乘客目的地";
        }
        return poi;
    }

    public static void callPhone(Context mContext, String userphone) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + userphone);
        intent.setData(data);
        mContext.startActivity(intent);
    }

    public static void vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }


    public static String getSha1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOrderState(Integer state) {
        String txt="";
        switch (state){

            case 0:
                txt="未抢单";
                break;
            case 1:
                txt="点击送达";
                break;
            case 2:
                txt="乘客已送达";
                break;
        }
        return  txt;

    }



}
