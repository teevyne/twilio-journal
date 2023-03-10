package com.twilio.joirnal.service;

import com.twilio.Twilio;
import com.twilio.joirnal.config.TwilioConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.net.PasswordAuthentication;
import java.util.Properties;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    public void sendSms(String to, String body) {
        Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());
        Message message = Message
                .creator(new PhoneNumber(to), new PhoneNumber(twilioConfig.getTwilioPhoneNumber()), body)
                .create();
    }

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", twilioConfig.getHost());
        props.put("mail.smtp.port", twilioConfig.getPort());
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(twilioConfig.getUsername(), twilioConfig.getPassword());
            }
        });

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(twilioConfig.getUsername()));
        message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(body);
        Transport.send(message);
    }
}

