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
}