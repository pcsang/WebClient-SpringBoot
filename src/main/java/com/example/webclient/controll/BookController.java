package com.example.webclient.controll;

import com.example.webclient.config.TrackingNumberGenerator;
import com.example.webclient.product.Book;
import com.example.webclient.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
@RequestMapping("/api/v2/client")
public class BookController {
    private final BookService bookService;
    private final WebClient webClient;

    @Autowired
    public BookController(BookService bookService, WebClient webClient) {
        this.bookService = bookService;
        this.webClient = webClient;
    }

    @GetMapping("/book")
    public List<Book> getBooks(@RequestParam(required = false) String author,
                               @RequestParam(required = false) String category) throws ExecutionException, InterruptedException {
        log.info("GET /book with author={}, category={}", author, category);
        return bookService.getAllBook(author, category);
    }


    @GetMapping("/book/{id}")
    public Book getBookById(@PathVariable int id) throws ExecutionException, InterruptedException {
        log.info("GET /book/{}", id);
        return bookService.getBookById(id);
    }


    @PostMapping("/book")
    public Book addBookNew(@RequestBody Book book) throws ExecutionException, JsonProcessingException, InterruptedException {
        log.info("POST /book data={}", book.toString());
        return bookService.postBook(book);
    }

    @DeleteMapping(path = "book/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) throws ExecutionException, InterruptedException {
        log.info("delete /book/{}", id);
        return bookService.deleteById(String.valueOf(id));
    }

    @PutMapping(path = "/book/update/{id}")
    public Book updateBookById(@PathVariable String id,
                                     @RequestBody Book book) throws ExecutionException, JsonProcessingException, InterruptedException {
        log.info("PUT url /book/update/{}", id);
        return bookService.updateBook(id, book);
    }

    @GetMapping("/abc")
    public Mono<String> getBooks(@RequestParam String trackingNumber){

        return webClient.get().uri("https://spx.vn/api/v2/fleet_order/tracking/search?sls_tracking_number="
                        + TrackingNumberGenerator.trackingNumberFunc(trackingNumber))
                .retrieve().bodyToMono(String.class);
    }
}
