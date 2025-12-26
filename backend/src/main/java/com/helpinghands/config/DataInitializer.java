package com.helpinghands.config;

import com.helpinghands.entity.Institution;
import com.helpinghands.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final InstitutionRepository institutionRepository;
    
    @Override
    public void run(String... args) {
        // Create host company if it doesn't exist
        if (institutionRepository.findByIsHostCompanyTrue().isEmpty()) {
            Institution hostCompany = Institution.builder()
                    .name("Helping Hands Host Company")
                    .description("The main host company that manages donations and distributes funds")
                    .isHostCompany(true)
                    .active(true)
                    .build();
            institutionRepository.save(hostCompany);
        }
    }
}

