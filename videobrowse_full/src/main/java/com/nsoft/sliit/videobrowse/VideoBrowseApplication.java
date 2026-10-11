package com.nsoft.sliit.videobrowse;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.nsoft.sliit.videobrowse.service.UserService;

@SpringBootApplication
public class VideoBrowseApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoBrowseApplication.class, args);
    }

    // create default roles + admin user for convenience
    @Bean
    CommandLineRunner init(UserService userService) {
        return args -> {
            try {
                userService.register("admin", "admin@example.com", "adminpass", true);
            } catch(Exception ignored){}
            try {
                userService.register("user", "user@example.com", "userpass", false);
            } catch(Exception ignored){}
        };
    }
}
