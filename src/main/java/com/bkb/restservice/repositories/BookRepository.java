package com.bkb.restservice.repositories;

import com.bkb.restservice.models.Book;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthor(String author);

    @Query("SELECT b FROM Book b")
    List<Book> dumpAllBooks();
}
