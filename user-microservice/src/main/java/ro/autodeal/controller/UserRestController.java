package ro.autodeal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.autodeal.dto.UserDto;
import ro.autodeal.model.User;
import ro.autodeal.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
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
}