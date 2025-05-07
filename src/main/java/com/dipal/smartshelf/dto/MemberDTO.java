package com.dipal.smartshelf.dto;

import com.dipal.smartshelf.enums.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Integer id;
    private String memberId;
    private String name;
    private String email;
    private LocalDateTime registrationDate;
    private MemberStatus status;
}