package com.example.webclient.rest;

import com.example.webclient.common.Constant;
import com.example.webclient.config.BookServiceConfig;
import com.example.webclient.config.UserDetailsConfig;
import com.example.webclient.product.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.reactive.function.client.WebClient;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class BookRestClient {
    private final RestClient restClient;
    private final BookServiceConfig config;
    private final WebClient webClient;
    private final UserDetailsConfig userDetailsConfig;

    @Value("")
    private String prefixUrl;
    @Value("")
    private String userInfo;
    @Value("")
    private String passInfo;

    /**
     * Constructor for BookService
     *
     * @param restClient        rest client
     * @param config            config is used to store info
     * @param webClient         web client use for Book
     * @param userDetailsConfig userDetailsConfig
     */
    @Autowired
    public BookRestClient(RestClient restClient,
                          BookServiceConfig config,
                          @Qualifier(Constant.WEB_CLIENT_BOOK) WebClient webClient,
                          UserDetailsConfig userDetailsConfig) {
        this.restClient = restClient;
        this.config = config;
        this.webClient = webClient;
        this.userDetailsConfig = userDetailsConfig;
    }

    @PostConstruct
    public void setConfig() {
        prefixUrl = config.getTypeHttp() + "://" + config.getHostname() + ":" + config.getPort()
                + "/" + config.getUrl();
        userInfo = userDetailsConfig.getUserInfo();
        passInfo = userDetailsConfig.getPassword();
    }

    @Async
    public CompletableFuture<ResponseEntity<Book>> getBookById(int id) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, userInfo);
        headerMap.put(Constant.AUTH_BASIC_PAS, passInfo);

        String url = prefixUrl + "/" + id;
        log.info("GET Url: {}", url);
        return restClient.invoke(url, headerMap, HttpMethod.GET, "", Book.class, webClient);
    }

    @Async
    public CompletableFuture<ResponseEntity<List<Book>>> getAllBooks() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, userInfo);
        headerMap.put(Constant.AUTH_BASIC_PAS, passInfo);
        log.info("GET Url: {}", prefixUrl);
        return restClient.invokeList(prefixUrl, headerMap, HttpMethod.GET, "", Book.class, webClient);
    }

    @Async
    public CompletableFuture<ResponseEntity<List<Book>>> getBookByFilters(String author, String category) {
        Map<String, String> params = new HashMap<>();
        if (!ObjectUtils.isEmpty(author)) {
            params.put("author", author);
        }

        if (!ObjectUtils.isEmpty(category)) {
            params.put("category", category);
        }

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, userInfo);
        headerMap.put(Constant.AUTH_BASIC_PAS, passInfo);
        String url = prefixUrl + "/by";

        log.info("GET Url: {}", url);

        return restClient.invokeList(url, headerMap, HttpMethod.GET, params, "", Book.class, webClient);
    }

    public CompletableFuture<ResponseEntity<Book>> postBook(Book book) throws JsonProcessingException {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, userInfo);
        headerMap.put(Constant.AUTH_BASIC_PAS, passInfo);

        ObjectMapper mapper = new ObjectMapper();
        String bodyJson = mapper.writeValueAsString(book);
        log.info("POST Url: {}, body={}", prefixUrl, bodyJson);
        return restClient.invoke(prefixUrl, headerMap, HttpMethod.POST, bodyJson, Book.class, webClient);
    }

    public CompletableFuture<ResponseEntity<String>> deleteById(String id) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, userInfo);
        headerMap.put(Constant.AUTH_BASIC_PAS, passInfo);
        String url = prefixUrl + "/" + id;
        log.info("DELETE Url: {}, id={}", prefixUrl, id);
        return restClient.invoke(url, headerMap, HttpMethod.DELETE, "", String.class, webClient);
    }

    public CompletableFuture<ResponseEntity<Book>> updateBookById(String id, Book book) throws JsonProcessingException {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, userInfo);
        headerMap.put(Constant.AUTH_BASIC_PAS, passInfo);
        String url = prefixUrl + "/" + id;

        ObjectMapper mapper = new ObjectMapper();
        String bodyJson = mapper.writeValueAsString(book);
        log.info("PUT Url: {}, body={}", url, bodyJson);

        return restClient.invoke(url, headerMap, HttpMethod.PUT, bodyJson, Book.class, webClient);
    }
}
