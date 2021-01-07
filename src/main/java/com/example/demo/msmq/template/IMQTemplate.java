package com.example.demo.msmq.template;

public interface IMQTemplate {

    void sendMessage(String queueName, String message);

    String receiveMessage(String queueName);
}
