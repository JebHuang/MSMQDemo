package com.example.demo.msmq.template.impl;

import com.example.demo.msmq.queue.MSMessageQueue;
import com.example.demo.msmq.template.IMQTemplate;
import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
public class MSMQTemplateImpl implements IMQTemplate {

    private Map<String, MSMessageQueue> queueMap;

    public MSMQTemplateImpl() {
    }

    public MSMQTemplateImpl(Map<String, MSMessageQueue> queueMap) {
        this.queueMap = queueMap;
    }

    @Override
    public void sendMessage(String queueName, String message) {
        // 1.find the queue
        // 2.send message to the queue
        MSMessageQueue queue = queueMap.get(queueName);
        if (null != queue) {
            if (log.isDebugEnabled()) log.debug("send msg: {}", message);
            queue.send(message);
        } else {
            // 2.1 log error can not find queue
            log.error("can not find queue named : {} ", queueName);
        }
    }

    @Override
    public String receiveMessage(String queueName) {
        // 1.find the queue
        // 2.receive message from the queue
        MSMessageQueue queue = queueMap.get(queueName);
        if (null != queue) {
            try {
                Message msg = queue.getMsmq().receive(500);
                if (null != msg) {
                    String message = msg.getMessage();
                    if (log.isDebugEnabled()) {
                        if (StringUtils.isEmpty(message)) {
                            log.debug("receive null msg");
                        } else {
                            log.debug("receive msg: {}", msg.getMessage());
                        }
                    }
                    return msg.getMessage();
                }
            } catch (MessageQueueException | UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            // 2.1 log error can not find queue
            log.error("can not find queue named : {} ", queueName);
        }
        return null;
    }

}
