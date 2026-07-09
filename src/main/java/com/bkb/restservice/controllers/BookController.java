package com.bkb.restservice.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bkb.restservice.models.Book;
import com.bkb.restservice.models.Book.BookRecord;
import com.bkb.restservice.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookRecord> getBooks() {
        return service.getBooks();
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return service.create(book);
    }
}