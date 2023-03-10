package com.twilio.joirnal.config;

import com.twilio.Twilio;
import com.twilio.http.TwilioRestClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class TwilioConfig {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.from.number}")
    private String twilioPhoneNumber;

    @Value("${twilio.port}")
    private String port;

    @Value("${twilio.host}")
    private String host;

    @Value("${twilio.username}")
    private String username;

    @Value("${twilio.password}")
    private String password;

    @Bean
    public TwilioRestClient initTwilio() {
        Twilio.init(accountSid, authToken);
        return Twilio.getRestClient();
    }
}

