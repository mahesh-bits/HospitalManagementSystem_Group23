package com.ss.notification.service.services;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    public void sendSms(String phoneNumber, String message) {
        // TODO: Integrate Twilio or any SMS provider
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
    }
}
