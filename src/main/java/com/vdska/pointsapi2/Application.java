package com.vdska.pointsapi2;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    private final RabbitAdmin rabbitAdmin;
    private final Queue otpQueue;
    private final Queue confirmQueue;

    public Application(RabbitAdmin rabbitAdmin, @Qualifier("otpQueue") Queue otpQueue, @Qualifier("confirmQueue") Queue confirmQueue) {
        this.rabbitAdmin = rabbitAdmin;
        this.otpQueue = otpQueue;
        this.confirmQueue = confirmQueue;
    }

    @PostConstruct
    public void init() {
        rabbitAdmin.declareQueue(otpQueue);
        rabbitAdmin.declareQueue(confirmQueue);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
