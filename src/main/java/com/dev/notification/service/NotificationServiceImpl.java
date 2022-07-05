package com.dev.notification.service;

import com.dev.notification.exception.NotificationException;
import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.PusherAuthError;
import com.pusher.pushnotifications.PusherValidationError;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final PushNotifications beamsClient;
    private static final String ERROR_MESSAGE_SERVER = "Problem with notification service.";
    private static final String ERROR_MESSAGE_ID = "Invalid instance id.";
    private static final String ERROR_MESSAGE_KEY = "Invalid secret key.";
    private static final String TITLE = "title";
    private static final String BODY = "body";
    private static final String FCM = "fcm";
    private static final String NOTIFICATION = "notification";
    private static final String ALERT = "alert";
    private static final String APS = "aps";
    private static final String APNS = "apns";

    public NotificationServiceImpl(PushNotifications beamsClient) {
        this.beamsClient = beamsClient;
    }

    public void pushCommonNotification() {
        publish("test-push", "Knowledge sharing",
                "Send push notification using Pusher Beams \u263a");
    }

    private void publish(String interest, String titleMessage, String notifMessage) {
        Map<String, String> fcmNotification = new HashMap<>();
        fcmNotification.put(TITLE, titleMessage);
        fcmNotification.put(BODY, notifMessage);
        Map<String, Map<String, String>> fcm = new HashMap<>();
        fcm.put(NOTIFICATION, fcmNotification);

        Map<String, String> apsAlert = new HashMap<>();
        apsAlert.put(TITLE, titleMessage);
        apsAlert.put(BODY, notifMessage);
        Map<String, Map<String, String>> alert = new HashMap<>();
        alert.put(ALERT, apsAlert);
        Map<String, Map<String, Map<String, String>>> aps = new HashMap<>();
        aps.put(APS, alert);

        Map<String, Map> publishRequest = new HashMap<>();
        publishRequest.put(FCM, fcm);
        publishRequest.put(APNS, aps);
        try {
            beamsClient.publishToInterests(List.of(interest), publishRequest);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            throw new NotificationException(ERROR_MESSAGE_SERVER);
        } catch (PusherValidationError e) {
            throw new NotificationException(ERROR_MESSAGE_ID);
        } catch (PusherAuthError e) {
            throw new NotificationException(ERROR_MESSAGE_KEY);
        }
    }
}
