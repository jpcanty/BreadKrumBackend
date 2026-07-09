package com.bkb.restservice.services;

import org.springframework.stereotype.Service;

import com.bkb.restservice.models.Book;
import com.bkb.restservice.models.Book.BookRecord;

import java.util.List;
import com.bkb.restservice.repositories.BookRepository;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<BookRecord> getBooks() {
        return repository.dumpAllBooks().stream().map(Book::toRecord).toList();
    }

    public Book create(Book book) {
        return book;
    }
}