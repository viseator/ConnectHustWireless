package com.viseator.connecthustwireless;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.WIFI_SERVICE;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class WifiStatesReceiver extends BroadcastReceiver {
    private ConnectHust connectHust;
    private static final String TAG = "viseator Receiver";
    private boolean firstToHust=true;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null) {
                Log.d(TAG, connectivityManager.getActiveNetworkInfo().getExtraInfo());
                if (connectivityManager.getActiveNetworkInfo().getExtraInfo().equals("\"HUST_WIRELESS\"") && firstToHust) {
                    firstToHust = false;
                    connectHust = new ConnectHust(context);
                    SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", MODE_PRIVATE);
                    connectHust.start(sharedPreferences);
                } else {
                    firstToHust = true;
                }
            }
        }
    }
}
