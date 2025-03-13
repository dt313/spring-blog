package com.blog.api.configuration;

import com.blog.api.entities.Permission;
import com.blog.api.entities.Role;
import com.blog.api.entities.User;
import com.blog.api.repository.PermissionRepository;
import com.blog.api.repository.RoleRepository;
import com.blog.api.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${spring.application.name}")
    String APP_NAME;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                            PermissionRepository permissionRepository,
                                            RoleRepository roleRepository) {
        return args -> {

            System.out.println("APP Starting ...................... BOT : " + APP_NAME);

                if(userRepository.findByUsername("admin").isEmpty()) {
                    Permission permission = new Permission("CREATE" , "create post");
                    Permission permission1 = new Permission("UPDATE" , "update post");
                    permissionRepository.save(permission);
                    permissionRepository.save(permission1);

                    Set<Permission> permissions = new HashSet<>();
                    permissions.add(permission1);
                    permissions.add(permission);


                    Role adminRole = new Role("ADMIN", "admin role", permissions);
                    Role userRole = new Role("USER", "user role", permissions);

                    roleRepository.save(adminRole);
                    roleRepository.save(userRole);
                    Set<Role> newRole = new HashSet<>();
                    newRole.add(adminRole);
                    User user = User.builder().username("admin").email("admin@gmail.com")
                            .password(passwordEncoder.encode("admin"))
                            .roles(newRole)
                            .build();

                    userRepository.save(user);
                    log.warn("admin user has been created with default password : admin , please change it");

                }
            };
        }


}
