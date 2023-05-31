package com.example.webclient.service;

import com.example.webclient.product.Book;
import com.example.webclient.rest.BookRestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BookService {
    private final BookRestClient restClient;

    /**
     * Constructor for BookService
     *
     * @param restClient rest client
     */
    @Autowired
    public BookService(BookRestClient restClient) {
        this.restClient = restClient;
    }

    public Mono<ResponseEntity<Book>> getBookById(int id) {
        Mono<ResponseEntity<Book>> response = restClient.getBookById(id);

        return response;
    }


}
