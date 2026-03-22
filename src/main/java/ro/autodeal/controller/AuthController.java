package ro.autodeal.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ro.autodeal.model.Role;
import ro.autodeal.model.User;
import ro.autodeal.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("roles", new Role[]{Role.VISITOR, Role.SELLER});
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               @RequestParam String name,
                               @RequestParam String phoneNumber,
                               @RequestParam Role role,
                               Model model,
                               HttpServletRequest request) {

        try {
            userService.registerUser(username, password, confirmPassword, name, phoneNumber, role);

            request.login(username, password);

            return "redirect:/cars";

        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("roles", new Role[]{Role.VISITOR, Role.SELLER});
            return "register";
        }
    }
}