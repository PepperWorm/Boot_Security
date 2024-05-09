package web.springBootSecurityProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.services.user.UserServiceImpl;

import java.security.Principal;

@Controller
@RequestMapping("/index")
public class UserController {

    private final UserServiceImpl service;

    @Autowired
    public UserController(UserServiceImpl service) {
        this.service = service;
    }

    @GetMapping
    public String helloPage(Model model, Principal principal) {
        String userName = principal.getName();
        User user = service.getUserByName(userName).orElse(null);

        if (user != null) {
            model.addAttribute("id", user.getId());
            model.addAttribute("name", user.getUsername());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("age", user.getAge());
            model.addAttribute("role", user.getRoles());
        }

        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        model.addAttribute("isAdmin", isAdmin);

        return "user";
    }
}
