package com.dipal.smartshelf.service;

import com.dipal.smartshelf.config.JWTService;
import com.dipal.smartshelf.dto.AuthRequest;
import com.dipal.smartshelf.dto.AuthResponse;
import com.dipal.smartshelf.entity.Role;
import com.dipal.smartshelf.entity.User;
import com.dipal.smartshelf.repository.RoleRepository;
import com.dipal.smartshelf.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public String register(AuthRequest request) {
        Role memberRole = roleRepository.findByName("MEMBER");
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(memberRole)
                .build();
        userRepository.save(user);
        return "User Registered Successfully";
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthResponse(jwtToken);
    }
}