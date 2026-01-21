package com.example.baseservice.dto.event;

import com.example.baseservice.filter.log.LogRequest;
import com.example.baseservice.filter.log.LogResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Setter
@Getter
public class AggregateLog extends ApplicationEvent {

    private String userId;

    private String username;

    private LogRequest logRequest;

    private LogResponse logResponse;

    public AggregateLog(Object source, String userId, String username, LogRequest logRequest, LogResponse logResponse) {
        super(source);
        this.userId = userId;
        this.username = username;
        this.logRequest = logRequest;
        this.logResponse = logResponse;
    }
}
