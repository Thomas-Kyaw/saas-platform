package com.thomaskyaw.subscriptionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {
    "com.thomaskyaw.subscriptionservice",
    "com.thomaskyaw.common"
})
@EnableScheduling
public class SubscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionServiceApplication.class, args);
    }
}
