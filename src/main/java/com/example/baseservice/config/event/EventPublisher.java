package com.example.baseservice.config.event;

import com.example.baseservice.dto.event.AggregateLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publish(AggregateLog event) {
        applicationEventPublisher.publishEvent(event);
    }

}
