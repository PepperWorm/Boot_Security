package web.springBootSecurityProject.services.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import web.springBootSecurityProject.models.User;
import web.springBootSecurityProject.repositories.UserRepositories;

import java.util.Optional;

@Service
public class UsersDetailService implements UserDetailsService {
    private final UserRepositories repositories;

    @Autowired
    public UsersDetailService(UserRepositories repositories) {
        this.repositories = repositories;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repositories.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Not found!");
        }
        return user.get();
    }
}
