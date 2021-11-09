package v.kiselev.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.Authentication;

@RequestMapping("/")
@RestController
public class LoginController {
    @GetMapping("/login")
    public User login(Authentication auth) {
        return (User) auth.getPrincipal();
    }
}
