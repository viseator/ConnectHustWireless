package com.viseator.connecthustwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class ConnectHust {
    private static final String TAG = "viseator ConnectHust";
    private NetworkTask networkTask;
    private SharedPreferences sharedPreferences;
    private Context context;
    private WifiManager wifiManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkTask.CONNECT_NETWORK:
                    handleResponse((String) msg.obj);
                    break;
                case NetworkTask.RESULT:
                    handleResult((String) msg.obj);
                    break;
                default:
            }
        }
    };


    public ConnectHust(Context context) {
        this.context = context;
        networkTask = new NetworkTask(handler);
        wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
    }

    public boolean checkStatus() {
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            Toast.makeText(context, "请开启wifi", Toast.LENGTH_SHORT).show();
            return false;
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d(TAG, wifiInfo.getSSID());
        if (!wifiInfo.getSSID().equals("\"HUST_WIRELESS\"")) {
            Toast.makeText(context, "正在连接到校园网", Toast.LENGTH_SHORT).show();
            List<ScanResult> scanResults = wifiManager.getScanResults();
            boolean findHust = false;
            for (ScanResult scanResult : scanResults) {
                Log.d(TAG, scanResult.SSID);
                if (scanResult.SSID.equals("HUST_WIRELESS")) findHust = true;
            }
            if (!findHust) {
                Toast.makeText(context, "未发现HUST_WIRELESS", Toast.LENGTH_SHORT).show();
                return false;
            }

            List<WifiConfiguration> wifiConfigurations = wifiManager.getConfiguredNetworks();
            int networkId = -1;
            for (WifiConfiguration wifiConfiguration : wifiConfigurations) {
                if (wifiConfiguration.SSID.equals("\"HUST_WIRELESS\"")) {
                    networkId = wifiConfiguration.networkId;
                    break;
                }
            }
            if (networkId == -1) {
                Toast.makeText(context, "请手动连接HUST_WIRELESS", Toast.LENGTH_LONG).show();
                return false;
            }

            wifiManager.enableNetwork(networkId, true);
            while (!wifiManager.getConnectionInfo().getSSID().equals("\"HUST_WIRELESS\"")) {
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return true;
        }
    }


    public void start(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        if (!wifiManager.getConnectionInfo().getSSID().equals("\"HUST_WIRELESS\"")) {
            Toast.makeText(context, "请连接到HUST_WIRELESS后重试", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(context, "测试连接...", Toast.LENGTH_LONG).show();
        networkTask.testNet();

    }

    private void handleResponse(String response) {

        if (response.contains("baidu")) {
            Toast.makeText(context, "现在可以正常上网", Toast.LENGTH_LONG).show();
        } else if (response.contains("eportal")) {
            Toast.makeText(context, "认证中", Toast.LENGTH_SHORT).show();
            String reg = "href=.*?\\?(.*?)'";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                String queryString = matcher.group(1);
                networkTask.startAuthenticate(queryString,
                        sharedPreferences.getString("userName", null),
                        sharedPreferences.getString("password", null));
            }
        }

    }

    private void handleResult(String result) {
        Gson gson = new Gson();
        ConnectResultBean resultBean = gson.fromJson(result, ConnectResultBean.class);
        if (resultBean.getResult().equals("fail")) {
            Toast.makeText(context, resultBean.getMessage(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "认证成功", Toast.LENGTH_SHORT).show();
        }
    }

}
