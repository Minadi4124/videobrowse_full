package com.nsoft.sliit.videobrowse.config;

import com.nsoft.sliit.videobrowse.entity.Role;
import com.nsoft.sliit.videobrowse.entity.User;
import com.nsoft.sliit.videobrowse.entity.MainCategory;
import com.nsoft.sliit.videobrowse.repository.CategoryRepository;
import com.nsoft.sliit.videobrowse.repository.RoleRepository;
import com.nsoft.sliit.videobrowse.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminSeeder {

    @Bean
    public ApplicationRunner seedAdmin(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, CategoryRepository categoryRepository) {
        return args -> {
            Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseGet(() -> roleRepository.save(new Role("ROLE_ADMIN")));

            userRepository.findByUsername("admin").orElseGet(() -> {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@sliitflix.local");
                admin.setPassword(encoder.encode("admin123"));
                admin.getRoles().add(userRole);
                admin.getRoles().add(adminRole);
                return userRepository.save(admin);
            });

            if (categoryRepository.count() == 0) {
                MainCategory c1 = new MainCategory();
                c1.setName("Education");
                c1.setDescription("Lectures, tutorials, and how‑tos");
                categoryRepository.save(c1);

                MainCategory c2 = new MainCategory();
                c2.setName("Entertainment");
                c2.setDescription("Movies, shows, and fun");
                categoryRepository.save(c2);

                MainCategory c3 = new MainCategory();
                c3.setName("Technology");
                c3.setDescription("Tech reviews and demos");
                categoryRepository.save(c3);

                MainCategory c4 = new MainCategory();
                c4.setName("Lifestyle");
                c4.setDescription("Vlogs, travel, food");
                categoryRepository.save(c4);
            }
        };
    }
}


