package com.dipal.smartshelf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowRequest {
    private Integer bookId;
    private Integer memberId;
    private LocalDateTime dueDate;
}