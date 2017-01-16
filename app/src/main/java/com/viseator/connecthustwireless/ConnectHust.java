package com.viseator.connecthustwireless;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class ConnectHust {
    private static final String TAG = "vir ConnectHust";
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
    }

    public void start(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        Toast.makeText(context, "测试连接...", Toast.LENGTH_SHORT).show();
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
