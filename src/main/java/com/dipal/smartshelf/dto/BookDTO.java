package com.dipal.smartshelf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {
    private Integer id;
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private Integer quantity;
    private Integer availableQuantity;
}