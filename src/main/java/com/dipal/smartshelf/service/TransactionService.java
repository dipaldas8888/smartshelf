package com.dipal.smartshelf.service;

import com.dipal.smartshelf.dto.BorrowRequest;
import com.dipal.smartshelf.dto.ReturnRequest;
import com.dipal.smartshelf.entity.Book;
import com.dipal.smartshelf.entity.Member;
import com.dipal.smartshelf.entity.Transaction;
import com.dipal.smartshelf.enums.MemberStatus;
import com.dipal.smartshelf.repository.BookRepository;
import com.dipal.smartshelf.repository.MemberRepository;
import com.dipal.smartshelf.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Transaction borrowBook(BorrowRequest borrowRequest) {
        Book book = bookRepository.findById(borrowRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + borrowRequest.getBookId()));
        Member member = memberRepository.findById(borrowRequest.getMemberId())
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + borrowRequest.getMemberId()));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new RuntimeException("Member is not active and cannot borrow books.");
        }

        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Book is currently unavailable.");
        }

        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setMember(member);
        transaction.setDueDate(borrowRequest.getDueDate());
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction returnBook(ReturnRequest returnRequest) {
        Book book = bookRepository.findById(returnRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + returnRequest.getBookId()));
        Transaction transaction = transactionRepository.findByMember_IdAndReturnDateIsNull(returnRequest.getMemberId())
                .stream()
                .filter(t -> t.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No active borrow transaction found for book id: " + returnRequest.getBookId() + " and member id: " + returnRequest.getMemberId()));

        if (transaction.getReturnDate() != null) {
            throw new RuntimeException("Book has already been returned.");
        }

        transaction.setReturnDate(LocalDateTime.now());
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    public List<Transaction> getTransactionsByMember(Integer memberId) {
        return transactionRepository.findByMember_Id(memberId);
    }

    public List<Transaction> getTransactionsByBook(Integer bookId) {
        return transactionRepository.findByBook_Id(bookId);
    }

    public List<Transaction> getOverdueTransactions() {
        return transactionRepository.findOverdueTransactions();
    }
}
