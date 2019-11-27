package com.example.mooka_umkm.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.example.mooka_umkm.R;
import com.example.mooka_umkm.screens.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by miftahun on 8/6/18.
 */


public class NotificationService {

    private static final String BACKCHECKING_REMINDER_NOTIFICATION_CHANNEL_ID = "notificy";
    private static final int BACKCHECKING_SUCCESS_PENDING_INTENT_ID = 317;
    private static final int WATER_REMINDER_NOTIFICATION_ID = 1;
    private static final String AUTH_KEY = "AAAANk3_R48:APA91bHfqvK4b7X6A2mRwHmmJv5zXeErAgCbekVrHvBrXg8pgDb_6zw6tNqTFCZIOV3pjjQhFI3_YGxGujVwnNlXp_ROK3DiMDTn3QzkIprTQk7SsHPU2aH67VTzxJXq8msp8fViQOWg";
    private final Context mContext;
    private final NotificationManager mNotificationManager;
    private static NotificationService instance;

    private static final String CHANNEL_NAME = "FCM";
    private static final String CHANNEL_DESC = "Firebase Cloud Messaging";

    public static NotificationService getInstance(Context context){
        if (instance == null ){
            instance = new NotificationService(context);
        }
        return instance;
    }

    // TODO : COmplete dependency Injection

    private NotificationService(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    BACKCHECKING_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    public void clearAllNotifications() {
        NotificationManager notificationManager = (NotificationManager)
                mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }



    public void backcheckingSelesaiNotification(String title, String body, String content){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mContext,"default")
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(contentIntent(mContext))
                .setLights(Color.RED, 1000, 300)
                .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                .setSmallIcon(R.mipmap.logo_mika)
                .setLargeIcon(largeIcon(mContext))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(channel);
        }

        assert mNotificationManager != null;
        mNotificationManager.notify(0, notificationBuilder.build());
    }

    private static PendingIntent contentIntent(Context context) {
        //oedubg
        Intent startActivityIntent = new Intent(context, MainActivity.class);
        startActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(
                context,
                BACKCHECKING_SUCCESS_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.mipmap.logo_mika);
        return largeIcon;
    }

    public void sendNotifToUmkm(final String umkm_id, final String namaBarang, final String title) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification(umkm_id, namaBarang, title);
            }
        }).start();
    }

    private void pushNotification(String umkm_id, String namaBarang, String title) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", title);
            jNotification.put("body",  namaBarang + "Telah terbeli oleh pelanggan");
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY_1");
            jNotification.put("icon", "ic_notification");

            jPayload.put("to", "/topics/" + umkm_id);
            jPayload.put("collapse_key", "type_a");
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                    Log.d("Debug : ",resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
}
