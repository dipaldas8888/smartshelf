package com.dipal.smartshelf.repository;

import com.dipal.smartshelf.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);

    @Query("SELECT b FROM Book b WHERE LOWER(b.title) LIKE %:query% OR LOWER(b.author) LIKE %:query% OR b.isbn LIKE %:query%")
    List<Book> searchBooks(@Param("query") String query);
}