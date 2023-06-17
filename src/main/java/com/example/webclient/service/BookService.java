package com.example.webclient.service;

import com.example.webclient.product.Book;
import com.example.webclient.rest.BookRestClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    /**
     * Get Book by id for user
     *
     * @param id Once
     * @return A Book
     * @throws ExecutionException   Execution Exception
     * @throws InterruptedException Interrupted Exception
     */
    public Book getBookById(int id) throws ExecutionException, InterruptedException {
        CompletableFuture<ResponseEntity<Book>> response = restClient.getBookById(id);
        response.join();
        ResponseEntity<Book> resBook = response.get();
        if (!resBook.getStatusCode().is2xxSuccessful() || resBook.getBody() == null) {
            log.warn("Response invalid - StatusCode = {}, Body = null", resBook.getStatusCode());
            return new Book();
        }
        return resBook.getBody();
    }

    public List<Book> getAllBook(String author, String category) throws ExecutionException, InterruptedException {
        ResponseEntity<List<Book>> response;
        if (ObjectUtils.isEmpty(author) && ObjectUtils.isEmpty(category)) {
            response = restClient.getAllBooks().get();
        } else {
            response = restClient.getBookByFilters(author, category).get();
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("Response invalid - StatusCode = {}, Body = null", response.getStatusCode());
            return Collections.emptyList();
        }
        return response.getBody();
    }

    public Book postBook(@NonNull Book book) throws JsonProcessingException, ExecutionException, InterruptedException {
        ResponseEntity<Book> response = restClient.postBook(book).get();
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("Response invalid - StatusCode = {}, Body = null", response.getStatusCode());
            return new Book();
        }
        return response.getBody();
    }

    public ResponseEntity<String> deleteById(String id) throws ExecutionException, InterruptedException {
        ResponseEntity<String> response = restClient.deleteById(id).get();
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("Response invalid - StatusCode = {}, Body = null", response.getStatusCode());
            return ResponseEntity.badRequest().body("Can not delete Book with id=" + id);
        }
        return response;
    }

    public Book updateBook(String id, @NonNull Book book) throws JsonProcessingException, ExecutionException, InterruptedException {
        ResponseEntity<Book> response = restClient.updateBookById(id, book).get();
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("Response invalid - StatusCode = {}, Body = null", response.getStatusCode());
            return new Book();
        }
        log.info("Update book successfully!");
        return response.getBody();
    }
}
