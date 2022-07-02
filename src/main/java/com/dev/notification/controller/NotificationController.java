package com.dev.notification.controller;

import com.dev.notification.service.NotificationServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notification")
@RestController
@Api(tags = "Notification")
public class NotificationController {
    private final NotificationServiceImpl notificationService;

    public NotificationController(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/common")
    @ApiOperation("Method to call the notification.")
    public void pushTimeTracking() {
        notificationService.pushCommonNotification();
    }
}
