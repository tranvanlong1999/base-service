package com.example.baseservice.event;

import com.example.baseservice.dto.event.AggregateLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingListener {

    @Async //TODO: Tìm hiểu về @Async
    @EventListener
    public void handle(AggregateLog event) {
        log.info("Received event: {}", event);
        //Save db
    }

}
