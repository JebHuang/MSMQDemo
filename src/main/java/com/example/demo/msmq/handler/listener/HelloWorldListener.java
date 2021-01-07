package com.example.demo.msmq.handler.listener;

import com.example.demo.msmq.handler.MQReceiveHandler;
import com.example.demo.msmq.handler.QueueListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.StringUtils;

@Slf4j
@QueueListener(queueName = "helloworld")
public class HelloWorldListener implements MQReceiveHandler {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Override
    public void run(String message) {
        if (!StringUtils.isEmpty(message)) {
            if (log.isDebugEnabled()) log.debug("send msg by ws , msg :{}", message);
            messagingTemplate.convertAndSend("/topic", message);
        }
    }
}
