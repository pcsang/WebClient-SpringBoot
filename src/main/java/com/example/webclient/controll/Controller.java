package com.example.webclient.controll;

import javax.annotation.PostConstruct;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.webclient.product.Book;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/api2/v1")
public class Controller {
    WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:9090/api/v1/books")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping("/books")
    public Flux<Book> getBooks() {
        return webClient.get().uri("")
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToFlux(Book.class);
    }

    @GetMapping("/book/{id}")
    public Mono<Book> getBookById(@PathVariable int id) {
        return webClient.get().uri("/"+id)
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToMono(Book.class);
    }

    @GetMapping(path = "/books_author_category")
    public Flux<Book> getBookByAuthor_Category(@RequestParam(required = false) String author,
                                               @RequestParam(required = false) String category) {
        return webClient.get().uri("/test?author="+author+"&category="+category)
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToFlux(Book.class);
    }

    @PostMapping("/book")
    public Mono<Book> addBookNew(@RequestBody Book book) {
        return webClient.post().uri("")
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .syncBody(book).retrieve().bodyToMono(Book.class);
    }

    @DeleteMapping(path = "/deletebook/{id}")
    public Mono<Book> deteleBook(@PathVariable int id) {
        return webClient.delete().uri("/"+id)
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToMono(Book.class);
    }

    @PutMapping(path = "/changebook/{id}")
    public Mono<Book> updateBookById(@PathVariable int id,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String author) {
        return webClient.put().uri("/"+id+"?"+"name="+name+"&author="+author)
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToMono(Book.class);
    }

    @GetMapping(path = "/countBook")
    public Mono<String> getCountBook(@RequestParam(required = false) String author) {
        return webClient.get().uri("/countBook?author="+author)
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToMono(String.class);
    }
}
