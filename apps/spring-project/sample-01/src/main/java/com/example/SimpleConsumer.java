package com.example;

import common.Foo1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SimpleConsumer {

    private final Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);

    private final TaskExecutor exec = new SimpleAsyncTaskExecutor();

    @KafkaListener(id = "fooGroup", topics = "topic1")
    public void listen(Foo1 foo) {
        logger.info("Received: {}" , foo);
        if (foo.foo().startsWith("fail")) {
            throw new RuntimeException("failed");
        }
        this.exec.execute(() -> System.out.println("Hit Enter to terminate...,topic1"));
    }

    @KafkaListener(id = "dltGroup", topics = "topic1-dlt")
    public void listenDlt(byte[] in) {
        logger.info("Received DLT: {}" , new String(in));
        this.exec.execute(() -> System.out.println("Hit Enter to terminate...,topic1-dlt"));
    }
}
