package web.springBootSecurityProject.services.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.springBootSecurityProject.models.Role;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.repositories.UserRepositories;
import web.springBootSecurityProject.services.role.RoleService;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    private final UserRepositories repositories;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepositories repositories, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.repositories = repositories;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repositories.save(user);
    }

    @Override
    public User getUserById(int id) {
        return repositories.findById(id).orElse(null);
    }

    @Override
    public Optional<User> getUserByName(String name) {
        return repositories.findByUsername(name);
    }

    @Override
    public List<User> getAllUsers() {
        return repositories.findAll();
    }

    @Transactional
    @Override
    public void update(int id, User user, String selectedRole) {
        user.setId(id);
        Role role = roleService.findRoleByName(selectedRole);
        roleService.save(role);
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repositories.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        repositories.deleteById(id);
    }

    @Override
    public void updatePassword(int id, String newPassword) {
        User user = repositories.findById(id).orElseThrow();
        user.setPassword(passwordEncoder.encode(newPassword));
        repositories.save(user);

    }
}
