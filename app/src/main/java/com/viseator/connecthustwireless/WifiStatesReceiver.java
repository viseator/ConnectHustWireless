package com.viseator.connecthustwireless;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            if (wifiManager.getConnectionInfo().getSSID().equals("\"HUST_WIRELESS\"")) {
                Log.d(TAG, "Received");
                connectHust = new ConnectHust(context);
                SharedPreferences sharedPreferences=context.getSharedPreferences("userInfo", MODE_PRIVATE);
                connectHust.start(sharedPreferences);

            }
        }
    }
}
