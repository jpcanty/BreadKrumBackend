package com.bkb.restservice.models;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
//public class Book {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String title;
//
//    private String author;
//
//    public Book() {}
//
//
//    // getters/setters
//}
public record Book( @Id Long id, String title, String author){
    public Book(String title, String author) {
        Book(0,title, author);
        this.author = author;
    }
<<<<<<< Updated upstream
}
=======

    public record BookRecord(Long id, String title, String author) {}

    public static BookRecord toRecord(Book book) {
        return new BookRecord(book.getId(), book.getTitle(), book.getAuthor());
    }

    Long getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    String getAuthor() {
        return author;
    }

    // getters/setters
}
>>>>>>> Stashed changes
