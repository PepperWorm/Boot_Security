package web.springBootSecurityProject.services.user;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    public UserServiceImpl(UserRepositories repositories, RoleService roleService) {
        this.repositories = repositories;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public void register(User user) {
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
    public void update(int id, User user) {
        user.setId(id);
        repositories.save(user);
    }

    @Transactional
    @Override
    public void deleteUserById(int id) {
        repositories.deleteById(id);
    }

    @Override
    public void updatePassword(int id, String newPassword) {
        User user = getUserById(id);
        if (user != null) {
            user.setPassword(newPassword);
            repositories.save(user);
        }
    }

    @Transactional
    @Override
    public Role getUserRole(String selectedRole) {
        if (selectedRole.equals("ADMIN")) {
            return roleService.findRoleByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role("ROLE_ADMIN");
                        roleService.save(role);
                        return role;
                    });
        } else {
            return roleService.findRoleByName("USER")
                    .orElseGet(() -> {
                        Role role = new Role("ROLE_USER");
                        roleService.save(role);
                        return role;
                    });
        }
    }

}
