package org.edx.mobile.notifications.services;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.edx.mobile.base.MainApplication;
import org.edx.mobile.core.IEdxEnvironment;
import org.edx.mobile.deeplink.PushLinkManager;
import org.edx.mobile.logger.Logger;

public class NotificationService extends FirebaseMessagingService {
    protected static final Logger logger = new Logger(NotificationService.class.getName());
    private static final String TAG = "FCM Service";

    @Override
    public void onNewToken(String s) {
        logger.debug("Refreshed FCM token: " + s);
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        final IEdxEnvironment environment = MainApplication.getEnvironment(this);

        if (environment.getConfig().areFirebasePushNotificationsEnabled()) {
            PushLinkManager.INSTANCE.onFCMForegroundNotificationReceived(remoteMessage);
        }

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }
}
