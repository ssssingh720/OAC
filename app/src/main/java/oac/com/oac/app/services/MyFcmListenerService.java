/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oac.com.oac.app.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import org.oacasia.R;
import oac.com.oac.app.activities.SplashScreen;
import oac.com.oac.app.manager.SharedPrefManager;
import oac.com.oac.app.modal.FeedParams;
import oac.com.oac.app.utils.Constants;


public class MyFcmListenerService extends FirebaseMessagingService {

    private static final String TAG = MyFcmListenerService.class.getSimpleName();
    private static int NotificationCount = 0;
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;
//Notification Body: tms_id :30149,tms_display_id :38825
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null
                && SharedPrefManager.getInstance().getSharedDataBoolean(FeedParams.NOTIFICATION_STATUS)) {
            String title = remoteMessage.getNotification().getTitle();
            Log.e(TAG, "Notification title: " + title);
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());

            String data = remoteMessage.getNotification().getBody();
//            data="tms_id :27629,tms_display_id :36588";
//            handleNotification(data);
//            sendNotification(title,data);
            Intent intent = new Intent(this, SplashScreen.class);
            showNotificationMessage(getApplicationContext(), title, data, "24/07/2018", intent);
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0
                && SharedPrefManager.getInstance().getSharedDataBoolean(FeedParams.NOTIFICATION_STATUS)) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                String tmsData = remoteMessage.getData().toString();
                JSONObject json = new JSONObject(tmsData);
                handleDataMessage(json);
                sendNotification("OAC", tmsData);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //UP 22 T0592
    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

//        try {
//            JSONObject data = json.getJSONObject("data");

        String title = json.optString("title", "Not found");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
        String imageUrl = json.optString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);


        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Constants.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", title);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), SplashScreen.class);
            resultIntent.putExtra("message", title);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, title, "24/07/2018", resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, title, "24/07/2018", resultIntent, imageUrl);
            }
        }
//        } catch (JSONException e) {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }


//    private static final String TAG = "OUTLOOK MAGS";
//    private int NotificationCount = 0;
//
//
//    // [START receive_message]
//    @Override
//    public void onMessageReceived(RemoteMessage message) {
//        String from = message.getFrom();
//        Map data = message.getData();
//    }
    // [END_EXCLUDE]

    // [END receive_message]

    private void sendNotification(String title, String message) {
//        final NotificationVo notification = new Gson().fromJson(message, NotificationVo.class);

        Intent intent = new Intent(this, SplashScreen.class);
        intent.putExtra(Constants.NOTIFICATION_MESSAGE, message);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, ++NotificationCount /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
        Bitmap bitmapLarge = BitmapFactory.decodeResource(getResources(),
                R.drawable.app_logo);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_logo)
                .setColor(Color.RED)
                .setLights(0xFF0000FF, 500, 500)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(++NotificationCount  /* ID of notification */, notificationBuilder.build());
    }
}
