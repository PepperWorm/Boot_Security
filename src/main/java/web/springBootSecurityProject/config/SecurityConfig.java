package web.springBootSecurityProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService service;
    private final SuccessUserHandler handler;

    public SecurityConfig(UserDetailsService service, SuccessUserHandler handler) {
        this.service = service;
        this.handler = handler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/admin/edit/**").hasRole("ADMIN")
                .antMatchers("/admin/delete/**").hasRole("ADMIN")
                .antMatchers("/index/**").hasAnyRole("USER", "ADMIN")
                .antMatchers("/auth/home", "/auth/login", "/auth/registration", "/error").permitAll()
                .anyRequest().hasAnyRole("ADMIN", "USER")
                .and()
                .formLogin()
                .loginPage("/auth/home") // Указать страницу входа
                .loginProcessingUrl("/process_login") // Указать URL для обработки входа
                .successHandler(handler) // Указать кастомный обработчик успешной аутентификации
                .failureUrl("/auth/login?error")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/auth/home")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
