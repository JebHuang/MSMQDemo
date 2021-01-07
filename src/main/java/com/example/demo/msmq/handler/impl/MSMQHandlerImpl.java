package com.example.demo.msmq.handler.impl;

import com.example.demo.msmq.handler.QueueListener;
import com.example.demo.msmq.handler.IMQHandler;
import com.example.demo.msmq.handler.MQReceiveHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class MSMQHandlerImpl implements IMQHandler, ApplicationContextAware {

    private Map<String, MQReceiveHandler> handlerMap;

    public MSMQHandlerImpl() {
    }

    public MSMQHandlerImpl(Map<String, MQReceiveHandler> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void handleReceiveMessage(String queueName, String message) {
        MQReceiveHandler handler = this.handlerMap.get(queueName);
        if (null != handler) {
            handler.run(message);
        }
    }

    /***
     * load queue listener to handle some message receive from queue
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> map = applicationContext.getBeansWithAnnotation(QueueListener.class);
        handlerMap = new ConcurrentHashMap<>();
        for (Object obj : map.values()) {
            Class clazz = obj.getClass();
            Annotation anno = clazz.getAnnotation(QueueListener.class);
            if (anno instanceof QueueListener) {
                QueueListener queueListener = (QueueListener) anno;
                String queueName = queueListener.queueName();
                MQReceiveHandler handler = (MQReceiveHandler) obj;
                handlerMap.put(queueName, handler);
            }
        }
    }
}
