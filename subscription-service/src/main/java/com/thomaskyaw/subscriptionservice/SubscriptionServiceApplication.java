package com.thomaskyaw.subscriptionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.thomaskyaw.subscriptionservice",
    "com.thomaskyaw.common"
})
public class SubscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubscriptionServiceApplication.class, args);
    }
}
