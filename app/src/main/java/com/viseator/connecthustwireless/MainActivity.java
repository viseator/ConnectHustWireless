package com.viseator.connecthustwireless;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "viseator MainActivity";
    @BindView(R.id.startButton)
    Button button;
    @BindView(R.id.startServiceButton)
    Button startService;
    @BindView(R.id.userName)
    EditText userName;
    @BindView(R.id.password)
    EditText password;
    SharedPreferences sharedPreferences;
    ConnectHust connectHust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermission();
        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        userName.setText(sharedPreferences.getString("userName", null));
        password.setText(sharedPreferences.getString("password", null));
        connectHust = new ConnectHust(this);
    }

    @OnClick(R.id.startButton)
    public void start() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.apply();
        if(connectHust.checkStatus()) connectHust.start(sharedPreferences);
    }

    @OnClick(R.id.startServiceButton)
    public void startService() {
        Intent intent = new Intent(this, AutoAuthenService.class);
        startService(intent);
    }


    public void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION},0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        checkPermission();
    }
}
