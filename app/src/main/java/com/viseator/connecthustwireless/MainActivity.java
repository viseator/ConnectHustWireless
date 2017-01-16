package com.viseator.connecthustwireless;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "vir MainActivity";
    @BindView(R.id.startButton)
    Button button;
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
        connectHust.start(sharedPreferences);
    }

}
