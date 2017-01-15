package com.viseator.connecthustwireless;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "vir MainActivity";
    @BindView(R.id.startButton)
    Button button;
    private NetworkTask networkTask;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.startButton)
    public void start() {
        networkTask = new NetworkTask(handler);
        networkTask.testNet(false);
    }

    private void handleResponse(String response) {

        if (response.contains("baidu")) {
            makeToast();
        } else if (response.contains("eportal")) {
            Toast.makeText(this, "认证中", Toast.LENGTH_SHORT).show();
            String reg = "href=.*?\\?(.*?)'";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(response);
            if (matcher.find()) {
                String queryString = matcher.group(1);
                Log.d(TAG, queryString);
                networkTask.startAuthenticate(queryString);
                networkTask.testNet(true);
            }


        }

    }

    private void makeToast() {
        Toast.makeText(this, "现在可以正常上网", Toast.LENGTH_LONG).show();
    }

}
