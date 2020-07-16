package com.Motawer.kalemah.Notifactions;

/*import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.Motawer.kalemah.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage)
    {
            super.onMessageReceived(remoteMessage);

            ShowNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }

    private void ShowNotification(String title, String body)
    {
        NotificationManager notificationManager
                =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        String NOTIFICATIONS_CHANNEL_ID ="com.Motawer.kalemah.test";


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
            {
                NotificationChannel notificationChannel =new NotificationChannel(NOTIFICATIONS_CHANNEL_ID,
                        "Notification",NotificationManager.IMPORTANCE_DEFAULT);

                notificationChannel.setDescription("Motawer Team");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.BLUE);
                notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
                notificationChannel.enableLights(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }

            NotificationCompat.Builder builder =new NotificationCompat.Builder(this,NOTIFICATIONS_CHANNEL_ID);
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.chat_icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setContentInfo("Info");

            notificationManager.notify(new Random().nextInt(),builder.build());


    }

    @Override
    public void onNewToken(@NonNull String s)
    {
        super.onNewToken(s);
        Log.d("TOKENFIREBASE",s);
    }
}*/

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.Motawer.kalemah.MainActivity;
import com.Motawer.kalemah.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService
{

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


        }

        sendNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());

    }

    private void sendNotification(String title, String body)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.chat_icon)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
