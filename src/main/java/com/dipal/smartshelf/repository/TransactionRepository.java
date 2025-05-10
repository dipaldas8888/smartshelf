package com.dipal.smartshelf.repository;

import com.dipal.smartshelf.entity.Book;
import com.dipal.smartshelf.entity.Member;
import com.dipal.smartshelf.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByMember_IdAndReturnDateIsNull(Integer memberId);

    @Query("SELECT t FROM Transaction t WHERE t.returnDate IS NULL AND t.dueDate < CURRENT_TIMESTAMP")
    List<Transaction> findOverdueTransactions();

    List<Transaction> findByBook_Id(Integer bookId);
    List<Transaction> findByMember_Id(Integer memberId);
    boolean existsByBook(Book book);
    boolean existsByMember(Member member);

}