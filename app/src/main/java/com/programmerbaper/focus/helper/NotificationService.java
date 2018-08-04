package com.programmerbaper.focus.helper;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.programmerbaper.focus.activities.MainActivity;

public class NotificationService extends NotificationListenerService {


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (MainActivity.mIsFocus) {
            NotificationService.this.cancelAllNotifications();
            MainActivity.mNotificationManager.cancelAll();
        }


    }

}
