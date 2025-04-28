package com.example;

import common.Foo1;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleProducer {

    private final KafkaTemplate<Object, Object> template;

    public SimpleProducer(KafkaTemplate<Object, Object> template) {
        this.template = template;
    }

    @PostMapping("/send/foo/{what}")
    public void sendFoo(@PathVariable String what) {
        template.send("topic1", new Foo1(what));
    }

    @PostMapping("/send/foo/1/{what}")
    public void sendFoo1(@PathVariable String what) {
        template.send("topic1-dlt", new Foo1(what));
    }

}
