package com.example.baseservice.common.helper;

import com.example.baseservice.common.exception.TimeoutException;
import com.example.baseservice.common.json.JsonUtils;
import com.example.baseservice.common.response.ApiRequest;
import com.example.baseservice.common.response.ApiResponse;
import com.example.baseservice.dto.OptionalCall;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.time.Duration;

import static com.example.baseservice.dto.OptionalCall.LOAD_BALANCED;
import static com.example.baseservice.dto.OptionalCall.REST_API;


@Slf4j
@Component
public class ServiceCaller implements ApplicationContextAware {

    private OptionalCall optionalCall;
    private boolean forwardHeader;
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private ServiceCaller() {
    }

    private ServiceCaller(OptionalCall optionalCall, boolean forwardHeader) {
        this.optionalCall = optionalCall;
        this.forwardHeader = forwardHeader;
    }

    public static ServiceCaller on() {
        return new ServiceCaller(REST_API, false);
    }

    public ServiceCaller optionalCall(OptionalCall optionalCall) {
        this.optionalCall = optionalCall;
        return this;
    }

    public ServiceCaller forwardHeader(boolean forwardHeader) {
        this.forwardHeader = forwardHeader;
        return this;
    }

    public ApiResponse callApi(ApiRequest apiRequest) {
        ApiResponse apiResponse;
        if (forwardHeader) ServiceHelper.forwardHeader(apiRequest);
        switch (optionalCall) {
            case REST_API -> {
                log.info("Call api with option mode: {}", REST_API);
                RestTemplate restTemplate = new RestTemplate();
                if (apiRequest.getConnectionTimeout() != null || apiRequest.getReadTimeout() != null) {
                    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
                    requestFactory.setConnectTimeout((int) ObjectUtils.defaultIfNull(apiRequest.getConnectionTimeout(), Duration.ZERO).toMillis());
                    restTemplate.setRequestFactory(requestFactory);
                }
                apiResponse = callWithRestApi(apiRequest, restTemplate);
            }
            case LOAD_BALANCED -> {
                log.info("Call api with option mode: {}", LOAD_BALANCED);
                RestTemplate restTemplate = context.getBean("loadBalancedRestTemplate", RestTemplate.class);
                apiResponse = callWithRestApi(apiRequest, restTemplate);
            }
            default -> {
                log.info("Call api with option default: {}", REST_API);
                RestTemplate restTemplate = new RestTemplate();
                apiResponse = callWithRestApi(apiRequest, restTemplate);
            }
        }
        return apiResponse;
    }

    private ApiResponse callWithRestApi(ApiRequest apiRequest, RestTemplate restTemplate) {
        try {
            String url = apiRequest.getHost().trim() + apiRequest.getPath().trim();
            log.info("[SPDV_REQUEST]: path: {}, requestBody: {}, header: {}, uriVariables: {}", url,
                    new ObjectMapper().writeValueAsString(apiRequest.getEntity().getBody()), apiRequest.getEntity().getHeaders(), apiRequest.getParameters());
            long startTime = System.currentTimeMillis();
            ResponseEntity<byte[]> result = restTemplate.exchange(url, apiRequest.getMethod(), apiRequest.getEntity(), byte[].class, apiRequest.getParameters());
            log.info("[SPDV_RESPONSE_TIME]: {}", (System.currentTimeMillis() - startTime));
            String responseBody = ServiceHelper.supportDecodeGzip(result);
            log.info("[SPDV_RESPONSE]: responseBody: {}", responseBody);
            return new ApiResponse(JsonUtils.fromJson(responseBody, JsonElement.class));
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            e.printStackTrace();
            throw e;
        } catch (ResourceAccessException e) {
            throw new TimeoutException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
