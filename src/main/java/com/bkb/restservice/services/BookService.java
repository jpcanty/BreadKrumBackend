package com.bkb.restservice.services;

import org.springframework.stereotype.Service;

import com.bkb.restservice.models.Book;
import java.util.List;
import com.bkb.restservice.repositories.BookRepository;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> getBooks() {
        List <Book> books = repository.dumpAllBooks();
        System.out.println();
        return books;
    }

    public Book create(Book book) {
        return book;
    }
}