package com.example.demo;

import com.example.demo.msmq.queue.MSMessageQueue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private MSMessageQueue MSMessageQueue;

    @Test
    void sendMSMQ() {
        String message = "good morning";
        MSMessageQueue.send(message);
    }
}
