package io.otherutils.tooles;

import android.util.Log;

/**
 * Created by Administrator on 2018-02-27.
 */

public class SysLog {
    public static void out(Object str){
        Log.v("syslog","-------------------------start-------------------------");
        Log.v("syslog",str.toString());
        Log.v("syslog","--------------------------end------------------------");

    }
}
