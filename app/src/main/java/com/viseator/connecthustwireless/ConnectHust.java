package com.viseator.connecthustwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class ConnectHust {
    private NetworkTask networkTask;
    private SharedPreferences sharedPreferences;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NetworkTask.CONNECT_NETWORK:
                    handleResponse((String) msg.obj);
                    break;
                case NetworkTask.TEST_NETWORK:
                    makeToast();
                default:
            }
        }
    };


    public ConnectHust(Context context) {
        this.context = context;
        networkTask = new NetworkTask(handler);
    }

    public void start(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        networkTask.testNet(false);
    }

    private void handleResponse(String response) {

        if (response.contains("baidu")) {
            makeToast();
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
                networkTask.testNet(true);
            }


        }

    }

    private void makeToast() {
        Toast.makeText(context, "现在可以正常上网", Toast.LENGTH_LONG).show();
    }
}
