package com.dipal.smartshelf.service;

import com.dipal.smartshelf.dto.MemberDTO;
import com.dipal.smartshelf.entity.Member;
import com.dipal.smartshelf.repository.MemberRepository;
import com.dipal.smartshelf.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public MemberDTO getMemberById(Integer id) {
        return memberRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    public MemberDTO registerMember(MemberDTO memberDTO) {
        Member member = convertToEntity(memberDTO);
        Member savedMember = memberRepository.save(member);
        return convertToDTO(savedMember);
    }

    public MemberDTO updateMember(Integer id, MemberDTO memberDTO) {
        Member existingMember = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        Member updatedMember = convertToEntity(memberDTO);
        updatedMember.setId(existingMember.getId()); // Ensure ID is retained
        Member savedMember = memberRepository.save(updatedMember);
        return convertToDTO(savedMember);
    }

    public void deleteMember(Integer id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        if(transactionRepository.existsByMember(member)) {
            throw new RuntimeException("Cannot delete member because they have associated transactions");
        }
        memberRepository.deleteById(id);
    }

    private MemberDTO convertToDTO(Member member) {
        return new MemberDTO(member.getId(), member.getMemberId(), member.getName(), member.getEmail(),
                member.getRegistrationDate(), member.getStatus());
    }

    private Member convertToEntity(MemberDTO memberDTO) {
        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setStatus(memberDTO.getStatus());
        return member;
    }
}