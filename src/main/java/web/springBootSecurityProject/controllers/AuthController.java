package web.springBootSecurityProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public String performRegistration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, @RequestParam("selectedRole") String selectedRole) {
        validator.validate(user, bindingResult);
        Role role = roleService.findRoleByName(selectedRole);
        roleService.save(role);
        user.getRoles().add(role);
        userService.register(user);

        return bindingResult.hasErrors() ? "/auth/registration" : "redirect:/auth/home";
    }
}
