package com.app.lfc.scooter.Service;

import android.content.Context;
import android.support.v4.app.NotificationCompat;

public class MyNotificationManager {
    Context context;
    static MyNotificationManager myNotificationManager;

    public MyNotificationManager(Context context) {
        this.context = context;
    }

    public static synchronized MyNotificationManager getInstance(Context context){
        if (myNotificationManager == null)
            myNotificationManager = new MyNotificationManager(context);
        return myNotificationManager;
    }
    public void desplayNotification(String title, String body){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

    }
}
