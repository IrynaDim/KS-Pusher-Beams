package com.dev.notification;

import com.dev.notification.exception.NotificationException;
import com.dev.notification.service.NotificationServiceImpl;
import com.pusher.pushnotifications.PushNotifications;
import com.pusher.pushnotifications.PusherAuthError;
import com.pusher.pushnotifications.PusherValidationError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {
    @InjectMocks
    private NotificationServiceImpl notificationServiceMock;
    @Mock
    private PushNotifications pushNotificationMock;

    @Captor
    private ArgumentCaptor<List<String>> listCaptor;
    @Captor
    private ArgumentCaptor<Map<String, Map>> mapCaptor;

    @Test
    void pushTimeNotification_argCapture_ok() throws Exception {
        notificationServiceMock.pushCommonNotification();
        verify(pushNotificationMock, times(1)).publishToInterests(listCaptor.capture(), mapCaptor.capture());
        assertThat(listCaptor.getValue()).isEqualTo(List.of("test-push"));
        assertThat(mapCaptor.getValue()).isEqualTo(getTimeTrackRequest());
    }

    @Test
    void pushNotification_not_correct_id_fail() throws IOException, URISyntaxException, InterruptedException {
        when(pushNotificationMock.publishToInterests(anyList(), anyMap())).thenThrow(PusherValidationError.class);
        assertThrows(NotificationException.class, notificationServiceMock::pushCommonNotification);
    }

    @Test
    void pushNotification_not_correct_key_fail() throws IOException, URISyntaxException, InterruptedException {
        when(pushNotificationMock.publishToInterests(anyList(), anyMap())).thenThrow(PusherAuthError.class);
        assertThrows(NotificationException.class, notificationServiceMock::pushCommonNotification);
    }

    @Test
    void pushNotification_server_fail() throws IOException, URISyntaxException, InterruptedException {
        when(pushNotificationMock.publishToInterests(anyList(), anyMap())).thenThrow(InterruptedException.class);
        assertThrows(NotificationException.class, notificationServiceMock::pushCommonNotification);
    }

    private Map<String, Map> getTimeTrackRequest() {
        Map<String, String> fcmNotificationTime = new HashMap<>() {{
            put("title", "Test");
            put("body", "Hi! It's just for test \u263a");
        }};

        Map<String, String> apsAlertTime = new HashMap<>() {{
            put("title", "Test");
            put("body", "Hi! It's just for test \u263a");
        }};
        return constructMap(fcmNotificationTime, apsAlertTime);
    }

    private Map<String, Map> constructMap(Map<String, String> fcmNotification, Map<String, String> apsAlert) {
        Map<String, Map<String, String>> fcmTime = new HashMap<>() {{
            put("notification", fcmNotification);
        }};
        Map<String, Map<String, String>> alert = new HashMap<>() {{
            put("alert", apsAlert);
        }};
        Map<String, Map<String, Map<String, String>>> aps = new HashMap<>() {{
            put("aps", alert);
        }};
        return new HashMap<>() {{
            put("fcm", fcmTime);
            put("apns", aps);
        }};
    }
}
