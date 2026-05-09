package ro.autodeal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.repository.UserRepository;
import ro.autodeal.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        userService = new UserService(userRepository, passwordEncoder);
    }

    @Test
    void registerUserShouldSaveUserWhenDataIsValid() {
        when(userRepository.findByUsername("seller2")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("1234")).thenReturn("encodedPassword");

        userService.registerUser(
                "seller2",
                "1234",
                "1234",
                "Seller Two",
                "0712345678",
                "seller2@example.com",
                Role.SELLER
        );

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUserShouldThrowExceptionWhenUsernameExists() {
        User existingUser = new User();
        existingUser.setUsername("seller1");

        when(userRepository.findByUsername("seller1")).thenReturn(Optional.of(existingUser));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(
                        "seller1",
                        "1234",
                        "1234",
                        "Seller One",
                        "0712345678",
                        "seller1@example.com",
                        Role.SELLER
                )
        );

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void registerUserShouldThrowExceptionWhenPasswordsDoNotMatch() {
        when(userRepository.findByUsername("seller3")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.registerUser(
                        "seller3",
                        "1234",
                        "wrong",
                        "Seller Three",
                        "0712345678",
                        "seller3@example.com",
                        Role.SELLER
                )
        );

        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    void getByUsernameShouldReturnUserWhenUserExists() {
        User user = new User();
        user.setUsername("admin");
        user.setRole(Role.ADMIN);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        User result = userService.getByUsername("admin");

        assertEquals("admin", result.getUsername());
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void getAllUsersShouldReturnUsersList() {
        User admin = new User();
        admin.setUsername("admin");

        User seller = new User();
        seller.setUsername("seller1");

        when(userRepository.findAll()).thenReturn(List.of(admin, seller));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
    }
}