package com.example.webclient.rest;

import com.example.webclient.common.Constant;
import com.example.webclient.config.BookServiceConfig;
import com.example.webclient.product.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class BookRestClient {
    private final RestClient restClient;
    private final BookServiceConfig config;
    private final WebClient webClient;

    @Value("")
    private String prefixUrl;

    /**
     * Constructor for BookService
     *
     *  @param restClient rest client
     * @param config      config is used to store info
     * @param webClient   web client use for Book
     */
    @Autowired
    public BookRestClient(RestClient restClient,
                          BookServiceConfig config,
                          @Qualifier(Constant.WEB_CLIENT_BOOK) WebClient webClient) {
        this.restClient = restClient;
        this.config = config;
        this.webClient = webClient;
    }

    @PostConstruct
    public void setConfig() {
        prefixUrl = config.getTypeHttp() + "://" + config.getHostname() + ":" + config.getPort() + "/" + config.getUrl();
    }

    public Mono<ResponseEntity<Book>> getBookById(int id) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(Constant.AUTH_BASIC_USERNAME, "sang");
        headerMap.put(Constant.AUTH_BASIC_PAS, "sangpass");

        String url = prefixUrl + "/" + id;
        log.info("Url: {}", url);
        return restClient.invoke(url, headerMap, HttpMethod.GET, "", Book.class, webClient);
    }
}
