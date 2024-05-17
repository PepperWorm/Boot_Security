package web.springBootSecurityProject.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.springBootSecurityProject.models.Role;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.services.role.RoleService;
import web.springBootSecurityProject.services.user.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "/admin/admin";
    }


    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }


    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "/admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String editSubmit(@PathVariable("id") int id, @ModelAttribute("user") User updateUser, @RequestParam("selectedRole") String selectedRole) {
        userService.update(id, updateUser, selectedRole);
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

        if (bindingResult.hasErrors()) {
            return "/admin/add";
        }
        Role role = roleService.findRoleByName(selectedRole);

        roleService.save(role);
        user.getRoles().add(role);

        // Сохранить пользователя через сервис
        userService.register(user);

        return "redirect:/admin";
    }

}
