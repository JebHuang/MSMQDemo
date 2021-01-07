package com.example.demo.msmq.handler;

@FunctionalInterface
public interface MQReceiveHandler {
    public abstract void run(String message);
}
