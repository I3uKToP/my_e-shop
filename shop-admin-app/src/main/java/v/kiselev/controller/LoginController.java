package v.kiselev.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/access_denied")
    public String accessDenied () {
        return "401";
    }

    @GetMapping("/login")
    public String loginPage() {
        return  "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "user_form";
    }

}
