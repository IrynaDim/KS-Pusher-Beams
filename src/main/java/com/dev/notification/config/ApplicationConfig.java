package com.dev.notification.config;

import com.pusher.pushnotifications.PushNotifications;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value(value = "${pusher.beams.id}")
    private String pusherId;

    @Value(value = "${pusher.beams.key}")
    private String pusherSecretKey;

    @Bean
    public PushNotifications pusher() {
        return new PushNotifications(pusherId, pusherSecretKey);
    }
}
