package com.viseator.connecthustwireless;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class VirAppWidgetProvider extends AppWidgetProvider {
    public static final String RECEIVE_CLICK = "ReceiveClick";
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
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, VirAppWidgetProvider.class);
            intent.setAction(RECEIVE_CLICK);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(RECEIVE_CLICK)) {
            this.context = context;
            sharedPreferences= context.getSharedPreferences("userInfo", MODE_PRIVATE);
            networkTask = new NetworkTask(handler);
            networkTask.testNet(false);
        }
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
//                Log.d(TAG, queryString);
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
