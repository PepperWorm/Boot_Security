package web.springBootSecurityProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.springBootSecurityProject.models.Role;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.services.role.RoleService;
import web.springBootSecurityProject.services.user.UserServiceImpl;
import web.springBootSecurityProject.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserValidator validator;
    private final UserServiceImpl userService;
    private final RoleService roleService;

    @Autowired
    public AuthController(UserValidator validator, UserServiceImpl userService, RoleService roleService) {
        this.validator = validator;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/home")
    public String homePage() {
        return "auth/home";
    }

    @GetMapping("/login")
    public String LoginPage() {
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);
        user.getRoles().add(roleService.findRoleByName("ROLE_USER")
                .orElseGet(() -> {
                    Role role = new Role("ROLE_USER");
                    roleService.save(role);
                    return role;
                }));

        userService.register(user);

        return bindingResult.hasErrors() ? "/auth/registration" : "redirect:/auth/home";
    }
}
