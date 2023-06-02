package com.example.webclient.controll;

import com.example.webclient.product.Book;
import com.example.webclient.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v2/client")
public class Controller {
    private WebClient webClient;
    private final BookService bookService;

    @Autowired
    public Controller(BookService bookService) {
        this.bookService = bookService;
    }

    @PostConstruct
    public void init(){
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8081/api/v1/books")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @GetMapping("/books")
    public List<Book> getBooks(){
        List<Book> list = new ArrayList<>();
        Flux<Book> a = (Flux<Book>) webClient.get().uri("")
                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
                .retrieve().bodyToFlux(Book.class).subscribe(p -> list.add(p));

        return list;
    }


    @GetMapping("/book/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) throws ExecutionException, InterruptedException {
        return bookService.getBookById(id);
    }

//    @GetMapping(path = "/books_author_category")
//    public Flux<Book> getBookByAuthor_Category(@RequestParam(required = false) String author,
//                                               @RequestParam(required = false) String category){
//        return webClient.get().uri("/test?author="+author+"&category="+category)
//                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
//                .retrieve().bodyToFlux(Book.class);
//    }
//
//    @PostMapping("/book")
//    public Mono<Book> addBookNew(@RequestBody Book book){
//        return webClient.post().uri("")
//                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
//                .syncBody(book).retrieve().bodyToMono(Book.class);
//    }
//
//    @DeleteMapping(path = "/deletebook/{id}")
//    public Mono<Book> deteleBook(@PathVariable int id){
//        return webClient.delete().uri("/"+id)
//                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
//                .retrieve().bodyToMono(Book.class);
//    }
//
//    @PutMapping(path = "/changebook/{id}")
//    public Mono<Book> updateBookById(@PathVariable int id,
//                                     @RequestParam(required = false) String name,
//                                     @RequestParam(required = false) String author){
//        return webClient.put().uri("/"+id+"?"+"name="+name+"&author="+author)
//                .headers(HttpHeaders->HttpHeaders.setBasicAuth("sang", "sangpass"))
//                .retrieve().bodyToMono(Book.class);
//    }
}
