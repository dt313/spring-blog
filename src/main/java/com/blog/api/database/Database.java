package com.blog.api.database;


import com.blog.api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Database {

    PasswordEncoder passwordEncoder;
    @Bean
    ApplicationRunner initDatabase(UserRepository userRepository) {
        return args -> {

        };
    }
}
