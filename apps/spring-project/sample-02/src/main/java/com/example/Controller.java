package com.example;

import com.example.common.Bar1;
import com.example.common.Foo1;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    private final KafkaTemplate<Object,Object> template;

    public Controller(KafkaTemplate<Object, Object> kafkaTemplate) {
        this.template = kafkaTemplate;
    }

    @PostMapping(path = "/send/foo/{what}")
    public void sendFoo(@PathVariable String what) {
        this.template.send("foos", new Foo1(what));
    }

    @PostMapping(path = "/send/bar/{what}")
    public void sendBar(@PathVariable String what) {
        this.template.send("bars", new Bar1(what));
    }

    @PostMapping(path = "/send/unknown/{what}")
    public void sendUnknown(@PathVariable String what) {
        this.template.send("bars", what);
    }

}
