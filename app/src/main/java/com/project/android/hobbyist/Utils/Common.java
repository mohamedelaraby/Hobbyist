package com.project.android.hobbyist.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by hassa on 7/11/2018.
 */

public class Common {
    static final String TAG = Common.class.getSimpleName();
    public static boolean isConnectedToInternet(Context context){
        Log.v(TAG, "isUserConnectedToInternet");
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null){
            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            Log.v(TAG, info.toString());
            for (int i = 0; i<info.length; i++){
                if (info[i].getState() == NetworkInfo.State.CONNECTED){
                    Log.v(TAG, "isUserConnectedToInternet: true");
                    return true;
                }

            }
        }

        return false;
    }
}
