// File: src/main/java/com/dipal/smartshelf/config/DataInitializer.java
package com.dipal.smartshelf.config;

import com.dipal.smartshelf.entity.Role;
import com.dipal.smartshelf.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findByName("MEMBER") == null) {
            Role member = new Role();
            member.setName("MEMBER");
            roleRepository.save(member);
        }
        if (roleRepository.findByName("ADMIN") == null) {
            Role admin = new Role();
            admin.setName("ADMIN");
            roleRepository.save(admin);
        }
    }
}