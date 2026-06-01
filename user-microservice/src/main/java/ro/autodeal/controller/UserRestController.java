package ro.autodeal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.autodeal.dto.UserDto;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:63342")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers()
                .stream()
                .map(UserDto::fromUser)
                .toList();
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @GetMapping("/{username}/role")
    public ResponseEntity<String> getUserRole(@PathVariable String username) {
        User user = userService.getByUsername(username);
        return ResponseEntity.ok(user.getRole().name());
    }

    @GetMapping("/{username}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable String username) {
        return ResponseEntity.ok(userService.usernameExists(username));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterRequest request) {
        userService.registerUser(
                request.getUsername(),
                request.getPassword(),
                request.getConfirmPassword(),
                request.getName(),
                request.getPhoneNumber(),
                request.getEmail(),
                request.getRole()
        );

        User user = userService.getByUsername(request.getUsername());
        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@RequestBody LoginRequest request) {
        User user = userService.loginUser(
                request.getUsername(),
                request.getPassword()
        );

        return ResponseEntity.ok(UserDto.fromUser(user));
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String confirmPassword;
        private String name;
        private String phoneNumber;
        private String email;
        private Role role;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Role getRole() {
            return role;
        }

        public void setRole(Role role) {
            this.role = role;
        }
    }

    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}