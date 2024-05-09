package web.springBootSecurityProject.services.user;

import web.springBootSecurityProject.models.Role;
import web.springBootSecurityProject.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(User user);
    User getUserById(int id);
    Optional<User> getUserByName(String name);
    List<User> getAllUsers();
    void update(int id, User user);
    void deleteUserById(int id);
    void updatePassword(int userId, String newPassword);
    Role getUserRole(String selectedRole);
}
