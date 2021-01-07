package com.example.demo.msmq.queue;

import ionic.Msmq.Message;
import ionic.Msmq.MessageQueueException;
import ionic.Msmq.Queue;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.UnsupportedEncodingException;

/****
 * the queue entity
 */
@Data
@Slf4j
public class MSMessageQueue {

    private Queue Msmq;

    private String ip;

    private String queueName;

    private String ipAddr;

    public MSMessageQueue(String ip, String queueName) {
        this.ip = ip;
        this.queueName = queueName;
    }

    /***
     * close MSMQ connection before contain close
     */
    @PreDestroy
    private void preDestory() {
        if (null != Msmq) {
            try {
                Msmq.close();
            } catch (MessageQueueException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /***
     * init MSMQ connection
     */
    @PostConstruct
    private void init() {
        if (null == Msmq) {
            // todo: do something before connect
            // try to connect MSMQ
            this.connect();
            // todo: do something after connect
        }
    }

    /***
     * connect method
     */
    private void connect() {
        try {
            Msmq = new Queue(this.getQueueFullName(this.queueName));
        } catch (MessageQueueException e) {
            log.error(e.getMessage(), e);
            String fullname = this.ip + "\\private$\\" + this.queueName;
            System.out.println("create (" + fullname + ")");
            String qLabel = "Created by " + this.getClass().getName() + ".java";
            boolean transactional = true;  // should the queue be transactional
            try {
                Queue.create(fullname, qLabel, transactional);
            } catch (MessageQueueException mqE) {
                log.error(mqE.getMessage(), mqE);
            }
        }
    }

    /***
     * send message to MSMQ
     * @param message
     * @return
     */
    public boolean send(String message) {
        boolean result = false;
        try {
            // do something before send
            this.checkOpen();

            // the transaction flag must agree with the transactional flavor of the queue.
            String mLabel = "inserted by " + this.getClass().getName() + ".java";

            String correlationID = "L:none";

            Message mqMessage = new Message(message, mLabel, correlationID);

            Msmq.send(mqMessage);

            result = true;
        } catch (MessageQueueException | UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        // todo: do something after send
        return result;
    }

    private void checkOpen()
            throws MessageQueueException {
        if (Msmq == null) throw new MessageQueueException("open a queue first!\n", -1);
    }

    private String getIpAddr() {
        return " [ip:" + this.ip + "]";
    }

    private String getQueueFullName(String queueShortName) {
        return getQueueFullName(".", queueShortName);
    }

    private String getQueueFullName(String hostname, String queueShortName) {
        String h1 = hostname;
        String a1 = "OS";
        if ((h1 == null) || h1.equals("")) h1 = ".";
        char[] c = h1.toCharArray();
        if ((c[0] >= '1')
                && (c[0] <= '9')) a1 = "TCP";

        return "DIRECT=" + a1 + ":" + h1 + "\\private$\\" + queueShortName;
    }
}
