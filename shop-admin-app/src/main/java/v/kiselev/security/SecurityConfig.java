package v.kiselev.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    public void authConfig(AuthenticationManagerBuilder auth,
                           PasswordEncoder passEncoder,
                           UserAuthService userAuthService) throws Exception {
        auth.inMemoryAuthentication().withUser("mem_super")
                .password(passEncoder.encode("123"))
                .roles("SUPERADMIN");

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userAuthService);
        provider.setPasswordEncoder(passEncoder);

        auth.authenticationProvider(provider);
    }

    @Configuration
    public static class UiWebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.
                    authorizeRequests()
                    .antMatchers("/**/*.css", "/**/*.js").permitAll()
                    .antMatchers("/product/**").hasAnyRole("ADMIN", "SUPERADMIN", "CONTENT")
                    .antMatchers("/user/**").hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers("/category/**").hasAnyRole("ADMIN", "SUPERADMIN")
                    .antMatchers("/index/**").hasAnyRole("ADMIN", "SUPERADMIN","CONTENT")
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/index")
                    .and()
                    .exceptionHandling()
                    .accessDeniedPage("/access_denied");
        }
    }
}
