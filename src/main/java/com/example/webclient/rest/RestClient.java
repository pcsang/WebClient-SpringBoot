package com.example.webclient.rest;

import com.example.webclient.common.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class RestClient {
    public <T> CompletableFuture<ResponseEntity<T>> invoke(String url, Map<String, String> headerMap, HttpMethod httpMethod, Object body, Class<T> type, WebClient webClient) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String user = encodeBasicAuth(headerMap);
        httpHeaders.set(Constant.AUTHENTICATION_KEY_HEADERS, Constant.AUTH_BASIC + user);

        return this.makeRequest(url, httpHeaders, httpMethod, body, type, webClient);
    }

    public <T> CompletableFuture<ResponseEntity<T>> invoke(String url, Map<String, String> headerMap, HttpMethod httpMethod, Map<String, String> paramsRequest, Object body, Class<T> type, WebClient webClient) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String user = encodeBasicAuth(headerMap);
        httpHeaders.set(Constant.AUTHENTICATION_KEY_HEADERS, Constant.AUTH_BASIC + user);

        String urlIncludeParams = createUrl(url, paramsRequest).toUriString();
        log.info("Uri - {}", urlIncludeParams);

        return this.makeRequest(urlIncludeParams, httpHeaders, httpMethod, body, type, webClient);
    }

    public <T> CompletableFuture<ResponseEntity<T>> makeRequest(String url, HttpHeaders headers, HttpMethod httpMethod, Object body, Class<T> type, WebClient webClient) {
        long startTime = System.currentTimeMillis();
        WebClient.RequestBodySpec requestBodySpec = webClient.method(httpMethod).uri(URI.create(url));
        if (!ObjectUtils.isEmpty(headers)) {
            requestBodySpec.headers((HttpHeaders headersCustom) -> headersCustom.addAll(headers));
        }

        if (!ObjectUtils.isEmpty(body)) {
            log.info("Request has body");
            requestBodySpec.body(Mono.just(body), String.class);
        }
        log.info("{} starting", httpMethod.name());
        CompletableFuture<ResponseEntity<T>> responseEntity = requestBodySpec.retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(requestBody -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), requestBody))))
                .toEntity(type).toFuture();
        log.info("Request finish - Time = {}", System.currentTimeMillis() - startTime);

        return responseEntity;
    }

    public <T> CompletableFuture<ResponseEntity<List<T>>> invokeList(String url, Map<String, String> headerMap, HttpMethod httpMethod, Object body, Class<T> type, WebClient webClient) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String user = encodeBasicAuth(headerMap);
        httpHeaders.set(Constant.AUTHENTICATION_KEY_HEADERS, Constant.AUTH_BASIC + user);

        return this.makeRequestList(url, httpHeaders, httpMethod, body, type, webClient);
    }

    public <T> CompletableFuture<ResponseEntity<List<T>>> invokeList(String url, Map<String, String> headerMap, HttpMethod httpMethod, Map<String, String> paramsRequest, Object body, Class<T> type, WebClient webClient) {
        HttpHeaders httpHeaders = new HttpHeaders();
        String user = encodeBasicAuth(headerMap);
        httpHeaders.set(Constant.AUTHENTICATION_KEY_HEADERS, Constant.AUTH_BASIC + user);
        String urlIncludeParams = createUrl(url, paramsRequest).toUriString();
        log.info("Uri - {}", urlIncludeParams);

        return this.makeRequestList(urlIncludeParams, httpHeaders, httpMethod, body, type, webClient);
    }

    public <T> CompletableFuture<ResponseEntity<List<T>>> makeRequestList(String url, HttpHeaders headers, HttpMethod httpMethod, Object body, Class<T> type, WebClient webClient) {
        long startTime = System.currentTimeMillis();
        WebClient.RequestBodySpec requestBodySpec = webClient.method(httpMethod).uri(URI.create(url));
        if (!ObjectUtils.isEmpty(headers)) {
            requestBodySpec.headers(headersCustom -> headersCustom.addAll(headers));
        }

        if (!ObjectUtils.isEmpty(body)) {
            log.info("Request has body");
            requestBodySpec.body(Mono.just(body), String.class);
        }
        log.info("{} starting", httpMethod.name());
        CompletableFuture<ResponseEntity<List<T>>> responseEntity = requestBodySpec.retrieve()
                .onStatus(HttpStatus::isError, clientResponse -> clientResponse.bodyToMono(String.class)
                        .flatMap(requestBody -> Mono.error(new ResponseStatusException(clientResponse.statusCode(), requestBody))))
                .toEntityList(type).toFuture();
        log.info("Request finish - Time = {}", System.currentTimeMillis() - startTime);

        return responseEntity;
    }

    private HttpHeaders setRequestHeader(Map<String, String> headerMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        Set<String> keys = headerMap.keySet();
        for (String key : keys) {
            httpHeaders.set(key, headerMap.get(key));
        }

        return httpHeaders;
    }

    private String encodeBasicAuth(Map<String, String> mapAuthBasic) {
        String username = mapAuthBasic.get(Constant.AUTH_BASIC_USERNAME);
        String password = mapAuthBasic.get(Constant.AUTH_BASIC_PAS);
        return HttpHeaders.encodeBasicAuth(username, password, null);
    }

    private UriComponentsBuilder createUrl(String url, Map<String, String> params) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        Map.Entry hashMap;
        for (Iterator<Map.Entry<String, String>> it = params.entrySet().iterator(); it.hasNext(); builder.queryParam(String.valueOf(hashMap.getKey()), hashMap.getValue())) {
            hashMap = it.next();

        }
        return builder;
    }
}
