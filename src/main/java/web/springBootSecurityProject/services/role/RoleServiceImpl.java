package web.springBootSecurityProject.services.role;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import web.springBootSecurityProject.models.Role;
import web.springBootSecurityProject.repositories.RoleRepositories;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepositories roleRepositories;

    public RoleServiceImpl(RoleRepositories roleRepositories) {
        this.roleRepositories = roleRepositories;
    }

    @Override
    public Optional<Role> findRoleByName(String name) {
        return roleRepositories.findByRoleName(name);
    }

    @Transactional
    @Override
    public void save(Role role) {
        roleRepositories.save(role);
    }
}
