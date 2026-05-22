package com.taskflow.task.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic taskAssignedTopic() {
        return TopicBuilder.name("task.assigned").partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic notificationResultTopic() {
        return TopicBuilder.name("notification.result").partitions(3).replicas(1).build();
    }
}
