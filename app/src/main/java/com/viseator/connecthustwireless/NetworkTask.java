package com.viseator.connecthustwireless;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by viseator on 1/14/17.
 */

public class NetworkTask {

    private static final String TAG = "vir NetworkTask";
    public static final String REQUEST_URL = "http://192.168.50.3:8080/eportal/InterFace.do?method=login";
    public static final int CONNECT_NETWORK = 0;
    public static final int TEST_NETWORK = 1;
    private String queryString;
    private Handler handler;
    boolean isTest;

    public NetworkTask(Handler handler) {
        this.handler = handler;
    }

    class TestNet implements Runnable {
        public void run() {
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://www.baidu.com/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                String head = urlConnection.getHeaderField("Location");
                String response;
                if (head != null) {
                    response = head;
                } else {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append('\n');
                    }
                    response = stringBuilder.toString();
                }
                Message msg = Message.obtain();
                msg.what = isTest ? TEST_NETWORK : CONNECT_NETWORK;
                msg.obj = response;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }
    }

    class StartAuthenticate implements Runnable {
        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("userId", "todo")
                    .add("password", "todo")//
                    .add("queryString", queryString)
                    .add("service", "")
                    .add("operatorPwd", "")
                    .add("validcode", "")
                    .build();
            Request request = new Request.Builder()
                    .url(REQUEST_URL)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, response.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void testNet(boolean isTest) {
        this.isTest = isTest;
        Thread thread = new Thread(new TestNet());
        thread.start();
    }

    public void startAuthenticate(String queryString) {
        this.queryString = queryString;
        Thread thread = new Thread(new StartAuthenticate());
        thread.start();
    }
}
