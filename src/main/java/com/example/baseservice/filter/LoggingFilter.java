package com.example.baseservice.filter;

import com.example.baseservice.common.helper.ServiceHelper;
import com.example.baseservice.common.json.JsonUtils;
import com.example.baseservice.config.event.EventPublisher;
import com.example.baseservice.dto.event.AggregateLog;
import com.example.baseservice.filter.log.LogRequest;
import com.example.baseservice.filter.log.LogResponse;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import java.io.IOException;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
@Component("requestLoggingFilter")
public class LoggingFilter extends AbstractRequestLoggingFilter {

    private final EventPublisher eventPublisher;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String requestBody = new String(StreamUtils.copyToByteArray(request.getInputStream()));
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request, requestBody);
        HttpResponseWrapper responseWrapper = new HttpResponseWrapper(response);
        try {
            super.doFilterInternal(requestWrapper, responseWrapper, filterChain);
            if (!request.getDispatcherType().equals(DispatcherType.ASYNC) || authentication == null ||
                    !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
                return;
            }
            final String responseBody = responseWrapper.getResponseBody();
            LogRequest logRequest = LogRequest.builder()
                    .path(requestWrapper.getRequestURL().toString())
                    .method(requestWrapper.getMethod())
                    .request(LogRequest.Request.builder()
                            .requestBody(JsonUtils.fromJson(requestBody, Map.class))
                            .headers(ServiceHelper.buildHeaderInfo(request))
                            .params(ServiceHelper.buildRequestParameters(request))
                            .build())
                    .build();
            LogResponse logResponse = LogResponse.builder()
                    .status(responseWrapper.getStatus())
                    .responseBody(JsonUtils.fromJson(responseBody, Map.class))
                    .build();
//			var jwt = (Jwt) authentication.getPrincipal();
//			var userId = jwt.getClaims().get("sub").toString();
//			var username = ObjectUtils.defaultIfNull(jwt.getClaims().get("user_name"), jwt.getClaims().get("name"));
            User user = (User) authentication.getPrincipal();
            eventPublisher.publish(new AggregateLog(this, null, user.getUsername(), logRequest, logResponse));
        } catch (Exception ex) {
            log.error("Logging error: ", ex);
        }
    }

    @Override
    protected void beforeRequest(@NonNull HttpServletRequest request, String message) {
    }

    @Override
    protected void afterRequest(@NonNull HttpServletRequest request, String message) {
    }
}
