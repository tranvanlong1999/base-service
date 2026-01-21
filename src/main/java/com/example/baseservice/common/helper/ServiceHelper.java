package com.example.baseservice.common.helper;


import com.example.baseservice.common.ConstantString;
import com.example.baseservice.common.response.ApiRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class ServiceHelper {

    public static Map<String, Object> buildHeaderInfo(HttpServletRequest request) {
        return Collections.list(request.getHeaderNames())
                .stream()
                .collect(Collectors.toMap(h -> h, request::getHeader));
    }

    public static Map<String, Object> buildRequestParameters(HttpServletRequest request) {
        return Collections.list(request.getParameterNames())
                .stream()
                .collect(Collectors.toMap(parameterName -> parameterName, (s) -> request.getParameterValues(s)[0]));
    }

    public static HttpHeaders buildHeaderInRequest(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Collections.list(request.getHeaderNames()).forEach((k) -> headers.add(k.toLowerCase(), request.getHeader(k)));
        return headers;
    }

    public static void forwardHeader(ApiRequest apiRequest) {
        if (apiRequest.getRequest() == null) {
            throw new IllegalStateException("HttpServletRequest is not null!");
        }
        HttpHeaders headers = ServiceHelper.buildHeaderInRequest(apiRequest.getRequest());
        //Ghi đè các thuộc tính header
        HttpHeaders headersOverride = apiRequest.getEntity().getHeaders();
        if (!headersOverride.isEmpty()) {
            headersOverride.toSingleValueMap().forEach(
                    (k, v) -> headers.put(k, Collections.singletonList(v)));
        }
        apiRequest.setEntity(new HttpEntity<>(apiRequest.getEntity().getBody(), headers));
    }

    public static String supportDecodeGzip(ResponseEntity<byte[]> result) throws IOException {
        byte[] bytes = Objects.requireNonNull(result.getBody());
        String responseBody;
        List<String> contentEncoding = result.getHeaders().get("Content-Encoding");
        if (contentEncoding != null && contentEncoding.contains(ConstantString.GZIP)) {
            responseBody = new String(gZipDecompress(bytes), StandardCharsets.UTF_8); //support decompressGzip
        } else {
            responseBody = new String(bytes, StandardCharsets.UTF_8);
        }
        return responseBody;
    }

    public static byte[] gZipDecompress(byte[] compressedData) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(compressedData);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            gzipInputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
