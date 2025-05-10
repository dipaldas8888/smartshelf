package com.dipal.smartshelf.service;

import com.dipal.smartshelf.dto.BookDTO;
import com.dipal.smartshelf.entity.Book;
import com.dipal.smartshelf.repository.BookRepository;
import com.dipal.smartshelf.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private  final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BookDTO getBookById(Integer id) {
        return bookRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Book book = convertToEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return convertToDTO(savedBook);
    }

    public BookDTO updateBook(Integer id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
        Book updatedBook = convertToEntity(bookDTO);
        updatedBook.setId(existingBook.getId()); // Ensure ID is retained
        Book savedBook = bookRepository.save(updatedBook);
        return convertToDTO(savedBook);
    }

    public void deleteBook(Integer id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        if (transactionRepository.existsByBook(book)) {
            throw new RuntimeException("Cannot delete book because it has associated transactions");
        }

        bookRepository.deleteById(id);
    }


    public List<BookDTO> searchBooks(String query) {
        return bookRepository.searchBooks(query.toLowerCase()).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getIsbn(),
                book.getPublicationYear(), book.getQuantity(), book.getAvailableQuantity());
    }

    private Book convertToEntity(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(bookDTO.getAuthor());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setQuantity(bookDTO.getQuantity());
        book.setAvailableQuantity(bookDTO.getAvailableQuantity());
        return book;
    }
}