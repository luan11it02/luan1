package com.app.lfc.scooter.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.app.lfc.scooter.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    NotificationCompat.Builder notificationcompat;
    int NOTIFICATION_ID = 282;

    public MyFirebaseMessagingService() {
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        getNotification(getApplicationContext(), remoteMessage.getNotification().getBody());
    }

    private void getNotification(Context context, String content){
        notificationcompat = new NotificationCompat.Builder(context);
        notificationcompat.setAutoCancel(true);

        notificationcompat.setSmallIcon(R.drawable.icon_scooter);
        notificationcompat.setContentTitle("Scootervietnam.vn");
        notificationcompat.setContentText(content);

        NotificationManager notificationService = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =  notificationcompat.build();
        notificationService.notify(NOTIFICATION_ID, notification);
    }
}
