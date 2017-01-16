package com.viseator.connecthustwireless;

import android.Manifest;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by viseator on 1/16/17.
 * Wu Di
 * Email: viseator@gmail.com
 */

public class VirAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "viseator WidgetProvider";
    public static final String RECEIVE_CLICK = "ReceiveClick";
    private ConnectHust connectHust;
    private SharedPreferences sharedPreferences;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i = 0; i < N; i++) {
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
            connectHust = new ConnectHust(context);
            sharedPreferences = context.getSharedPreferences("userInfo", MODE_PRIVATE);
            if (connectHust.checkStatus()) connectHust.start(sharedPreferences);

        }
    }


}

