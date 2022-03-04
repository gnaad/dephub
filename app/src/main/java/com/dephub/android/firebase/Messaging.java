package com.dephub.android.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.dephub.android.R;
import com.dephub.android.activity.SplashScreen;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Messaging extends FirebaseMessagingService {

    private static final String TAG = "General";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);

        Log.d(TAG,"From: " + remoteMessage.getFrom( ));
        if (remoteMessage.getData( ).size( ) > 0) {
            Log.d(TAG,"Message data payload: " + remoteMessage.getData( ));
            if (remoteMessage.getNotification( ) != null) {
                Log.d(TAG,"Message Notification Body: " + remoteMessage.getNotification( ).getBody( ));
            }
        }
        //noinspection ConstantConditions
        sendNotification(remoteMessage.getNotification( ).getTitle( ),remoteMessage.getNotification( ).getBody( ),remoteMessage.getNotification( ).getClickAction( ));
    }

    private void sendNotification(String title,String messageBody,String click) {
        Intent intent = new Intent(this,SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Bitmap icon = BitmapFactory.decodeResource(getResources( ),R.mipmap.ic_launcher_round);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"General")
                .setSmallIcon(R.mipmap.ic_logowithoutbackground)
                .setLargeIcon(icon)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(title)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentText(messageBody)
                .setColor(ContextCompat.getColor(this,R.color.blue))
                .setAutoCancel(true)
                .setLights(getResources( ).getColor(R.color.blue),500,500)
                .setGroup("Unread Notification")
                .setGroupSummary(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("General","General",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0,notificationBuilder.build( ));
    }
}