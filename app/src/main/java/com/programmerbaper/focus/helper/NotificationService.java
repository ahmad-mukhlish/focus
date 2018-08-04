package com.programmerbaper.focus.helper;

import android.app.Notification;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.programmerbaper.focus.activities.MainActivity;
import com.programmerbaper.fokus.R;

public class NotificationService extends NotificationListenerService {


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (MainActivity.mIsFocus) {
            NotificationService.this.cancelAllNotifications();
            MainActivity.mNotificationManager.cancelAll();
        }


    }

}
