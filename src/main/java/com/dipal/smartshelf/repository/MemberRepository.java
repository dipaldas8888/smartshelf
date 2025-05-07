package com.dipal.smartshelf.repository;

import com.dipal.smartshelf.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    // Custom queries if needed
}