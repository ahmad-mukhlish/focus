package com.programmerbaper.focus.helper;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService {

    public static String TAG = "lalal";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {


        Log.i(TAG, "ID:" + sbn.getId());
        Log.i(TAG, "Posted by:" + sbn.getPackageName());
        Log.i(TAG, "tickerText:" + sbn.getNotification().tickerText);


    }

}
