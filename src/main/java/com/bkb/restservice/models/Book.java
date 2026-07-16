package com.bkb.restservice.models;

import jakarta.persistence.*;

//public class Book {
//

//
//    public Book() {}
//
//
//    // getters/setters
//}

@Entity
@Table(name = "books")
public class Book {
       @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String title;

   private String author;

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
