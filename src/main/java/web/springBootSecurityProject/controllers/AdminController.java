package web.springBootSecurityProject.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.services.user.UserService;
import web.springBootSecurityProject.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final UserValidator validator;

    @Autowired
    public AdminController(UserService userService, UserValidator validator) {
        this.userService = userService;
        this.validator = validator;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") int id, @ModelAttribute("user") User updateUser) {
        userService.update(id, updateUser);
        return "redirect:/admin";
    }

    @GetMapping("/add")
    public String registrationPage(@ModelAttribute("user") User user) {
        return "/admin/add";
    }

    @PostMapping("/add")
    public String performRegistration(
            @ModelAttribute("user") @Valid User user,
            BindingResult bindingResult,
            @RequestParam("selectedRole") String selectedRole) {

        validator.validate(user, bindingResult);
        user.getRoles().add(userService.getUserRole(selectedRole));
        userService.register(user);

        return bindingResult.hasErrors() ? "/admin/add" : "redirect:/admin";
    }

}
