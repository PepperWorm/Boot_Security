package web.springBootSecurityProject.services.role;

import web.springBootSecurityProject.models.Role;

public interface RoleService {
    Role findRoleByName(String name);
    void save(Role role);

}
