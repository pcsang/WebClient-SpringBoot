package com.example.webclient.controll;

import com.example.webclient.product.Book;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/api/v2/client")
public class Controller {
    WebClient webClient;

    @PostConstruct
    public void init(){
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/api/v1/books")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping("/books")
    public Flux<Book> getBooks(){
        return webClient.get().uri("").retrieve().bodyToFlux(Book.class);
    }

    @GetMapping("/book/{id}")
    public Mono<Book> getBookById(@PathVariable int id){
        return webClient.get().uri("/"+id).retrieve().bodyToMono(Book.class);
    }

    @GetMapping(path = "/books_author_category")
    public Flux<Book> getBookByAuthor_Category(@RequestParam(required = false) String author,
                                               @RequestParam(required = false) String category){
        return webClient.get().uri("/test?author="+author+"&category="+category).retrieve().bodyToFlux(Book.class);
    }

    @PostMapping("/book")
    public Mono<Book> addBookNew(@RequestBody Book book){
        return webClient.post().uri("").syncBody(book).retrieve().bodyToMono(Book.class);
    }

    @DeleteMapping(path = "/deletebook/{id}")
    public Mono<Book> deteleBook(@PathVariable int id){
        return webClient.delete().uri("/"+id).retrieve().bodyToMono(Book.class);
    }

    @PutMapping(path = "/changebook/{id}")
    public Mono<Book> updateBookById(@PathVariable int id,
                                     @RequestParam(required = false) String name,
                                     @RequestParam(required = false) String author){
        return webClient.put().uri("/"+id+"?"+"name="+name+"&author="+author).retrieve().bodyToMono(Book.class);
    }
}
