package com.example;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.messaging.Message;

import java.util.Map;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }

    static final String PAGE_VIEWS_TOPIC = "page-views";

}


@Configuration
class RunnerConfig {


    void kafka(KafkaTemplate<Object, Object> template) {
        var pageView = new PageView("index.html", 190L, "@adesh", "github");
        template.send(ProducerApplication.PAGE_VIEWS_TOPIC, pageView);
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> runnerListener(KafkaTemplate<Object, Object> template) {
        return event -> kafka(template);
    }
}


@Configuration
class KafkaConfig {

    @KafkaListener(topics = ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)
    public void listen(Message<PageView> pageView) {
        System.out.println("-----------------------");
        System.out.println("new page view " + pageView.getPayload());
        pageView.getHeaders().forEach((k, v) -> System.out.println(k + ":" + v));
    }


    @Bean
    NewTopic pageViewsTopic() {
        return new NewTopic(ProducerApplication.PAGE_VIEWS_TOPIC, 1, (short) 1);
    }

    @Bean
    KafkaTemplate<Object, Object> kafkaTemplate(ProducerFactory<Object, Object> producerFactory) {
        return new KafkaTemplate<>(producerFactory,
                Map.of(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class));
    }
}

record PageView(String page, long duration, String userId, String source) {
}