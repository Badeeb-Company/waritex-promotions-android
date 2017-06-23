package com.badeeb.waritex.network;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.badeeb.waritex.MainActivity;
import com.badeeb.waritex.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by ahmed on 6/19/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    // Logging Purpose
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private final static AtomicInteger notificationId = new AtomicInteger(0);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "onMessageReceived - End");

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "onMessageReceived - Message Notification Body: " + remoteMessage.getNotification().getBody());

            notificationReceiveProcessing(remoteMessage.getNotification());
        }

        Log.d(TAG, "onMessageReceived - End");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param notification FCM message received.
     */
    private void notificationReceiveProcessing(RemoteMessage.Notification notification) {

        Log.d(TAG, "notificationReceiveProcessing - Start");

        // in the below intent we define the target intent that the notification onclick will navigate to
        /*
        * Get the current visible fragment and set it in the parameter below
        * */
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // defining the destination intent that will be passed to the notification manager
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code for the sender */,
                intent /*this is the target intent*/,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent); // passing the destination intent

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(notificationId.incrementAndGet() /* incremented id*/, notificationBuilder.build());

        Log.d(TAG, "notificationReceiveProcessing - End");
    }
}
