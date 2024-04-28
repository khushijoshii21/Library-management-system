package com.librarymanagement.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

import com.twilio.rest.api.v2010.account.Message;

@RestController
public class WebhookController {
    @PostMapping("/webhook")
    public void handleTwilioWebhook(
        @RequestParam("From") String from,
        @RequestParam("To") String to,
        @RequestParam("Body") String body
    ) {
        String senderNumber = from;
        String messageBody = body;

        String response = processIncomingMessage(messageBody, senderNumber);

        Message message = Message.creator(
        new com.twilio.type.PhoneNumber(senderNumber),
        new com.twilio.type.PhoneNumber("whatsapp:+14155238886"),
        response)

        .create();
        System.out.println(message.getSid());
    }
    private String processIncomingMessage(String messageBody, String senderNumber) {
        String[] arrOfStr = senderNumber.split(":", 2);
        return Controller.Handler(messageBody, arrOfStr[1]);
    }
}

