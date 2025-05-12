package com.dipal.smartshelf.controller;

import com.dipal.smartshelf.dto.BorrowRequest;
import com.dipal.smartshelf.dto.ErrorResponse;
import com.dipal.smartshelf.dto.ReturnRequest;
import com.dipal.smartshelf.entity.Transaction;
import com.dipal.smartshelf.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/borrow")
    public ResponseEntity<Transaction> borrowBook(@RequestBody BorrowRequest borrowRequest) {
        return new ResponseEntity<>(transactionService.borrowBook(borrowRequest), HttpStatus.CREATED);
    }

    @PostMapping("/return")
    public ResponseEntity<Transaction> returnBook(@RequestBody ReturnRequest returnRequest) {
        return ResponseEntity.ok(transactionService.returnBook(returnRequest));
    }
    @PutMapping("/return/{id}")
    public ResponseEntity<?> returnBook(@PathVariable Integer id) {
        try {
            transactionService.returnBook(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Integer id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<Transaction>> getTransactionsByMember(@PathVariable Integer memberId) {
        return ResponseEntity.ok(transactionService.getTransactionsByMember(memberId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Transaction>> getTransactionsByBook(@PathVariable Integer bookId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBook(bookId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<Transaction>> getOverdueTransactions() {
        return ResponseEntity.ok(transactionService.getOverdueTransactions());
    }
}