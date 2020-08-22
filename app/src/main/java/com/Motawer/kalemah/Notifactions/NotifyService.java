package com.Motawer.kalemah.Notifactions;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.Motawer.kalemah.MainActivity;
import com.Motawer.kalemah.R;


public class NotifyService extends Service
{

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify = new NotificationCompat.Builder(this)
                .setContentTitle("Title")
                .setContentText("Hello World!")
                .setSmallIcon(R.drawable.google_icon)
                .setContentIntent(pIntent)
                .setSound(sound)
                .setAutoCancel(true)
                .build();


        mNM.notify(1, mNotify);

    }
}
