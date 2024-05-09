package web.springBootSecurityProject.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.services.user.UserServiceImpl;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserServiceImpl service;
    @Autowired
    public UserValidator(UserServiceImpl service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Optional<User> user = service.getUserByName(((User) target).getUsername());
        if (user.isEmpty()) {
            return;
        }
        errors.rejectValue("username", "", "User with this name hav been");
    }
}
