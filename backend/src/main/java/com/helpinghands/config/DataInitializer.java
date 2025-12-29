package com.helpinghands.config;

import com.helpinghands.entity.Institution;
import com.helpinghands.entity.User;
import com.helpinghands.repository.InstitutionRepository;
import com.helpinghands.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final InstitutionRepository institutionRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // Create host company if it doesn't exist
        createHostInstitution();
        createHostAdmin();
    }

    private void createHostAdmin() {
        if (userRepository.findAdminUsers().isEmpty()) {
            User user = User.builder()
                    .email("admin")
                    .createdAt(LocalDateTime.now())
                    .name("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(User.Role.ADMIN)
                    .build();
            userRepository.save(user);
        }
    }

    private Institution createHostInstitution() {
        Optional<Institution> optHostCompany = institutionRepository.findByIsHostCompanyTrue();
        if (optHostCompany.isEmpty()) {
            Institution hostCompany = Institution.builder()
                    .name("Helping Hands Host Company")
                    .description("The main host company that manages donations and distributes funds")
                    .isHostCompany(true)
                    .active(true)
                    .build();
            return institutionRepository.save(hostCompany);
        }
        return optHostCompany.get();
    }
}

