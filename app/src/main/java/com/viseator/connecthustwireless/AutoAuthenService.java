package com.viseator.connecthustwireless;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class AutoAuthenService extends Service {

    private WifiStatesReceiver wifiStatesReceiver;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wifiStatesReceiver = new WifiStatesReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(wifiStatesReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiStatesReceiver);
    }
}
