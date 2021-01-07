package com.example.demo.msmq;

import com.example.demo.msmq.handler.IMQHandler;
import com.example.demo.msmq.template.IMQTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MSMQScheduleJob {

    @Autowired
    private MSMQConfig msmqConfig;

    @Autowired
    private IMQTemplate mqTemplate;

    @Autowired
    private IMQHandler mqHandler;

    /**
     * 每1秒执行1次
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void executeFileDownLoadTask() {
        try {
            String message = mqTemplate.receiveMessage(msmqConfig.getQueueName());
            mqHandler.handleReceiveMessage(msmqConfig.getQueueName(), message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
