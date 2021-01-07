package com.example.demo.msmq.handler;

/***
 * the interface for handle message queue
 */
public interface IMQHandler {
    void handleReceiveMessage(String queueName, String message);
}
