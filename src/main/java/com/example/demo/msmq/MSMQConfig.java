package com.example.demo.msmq;

import com.example.demo.msmq.queue.MSMessageQueue;
import com.example.demo.msmq.template.IMQTemplate;
import com.example.demo.msmq.template.impl.MSMQTemplateImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class MSMQConfig {

    @Value("${queue.host}")
    @Getter
    private String queueHost;

    @Value("${queue.name}")
    @Getter
    private String queueName;

    @Bean
    public MSMessageQueue msMessageQueue() {
        return new MSMessageQueue(queueHost, queueName);
    }

    @Bean
    public IMQTemplate mqTemplate(MSMessageQueue msMessageQueue) {
        Map<String, MSMessageQueue> queueMap = new ConcurrentHashMap<>();
        queueMap.put(msMessageQueue.getQueueName(), msMessageQueue);
        return new MSMQTemplateImpl(queueMap);
    }
}
