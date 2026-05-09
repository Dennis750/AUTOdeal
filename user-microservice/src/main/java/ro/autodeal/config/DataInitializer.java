package ro.autodeal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.repository.UserRepository;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {
            createAdminIfMissing();
            createSellerIfMissing();
        };
    }

    private void createAdminIfMissing() {
        userRepository.findByUsername("admin").orElseGet(() -> {
            User user = new User();
            user.setUsername("admin");
            user.setPassword(passwordEncoder.encode("admin123"));
            user.setName("AUTOdeal Admin");
            user.setPhoneNumber("0700000000");
            user.setEmail("test@example.com");
            user.setRole(Role.ADMIN);
            return userRepository.save(user);
        });
    }

    private void createSellerIfMissing() {
        userRepository.findByUsername("seller1").orElseGet(() -> {
            User user = new User();
            user.setUsername("seller1");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setName("Autoklass Cluj");
            user.setPhoneNumber("0744123456");
            user.setEmail("test@example.com");
            user.setRole(Role.SELLER);
            return userRepository.save(user);
        });
    }
}