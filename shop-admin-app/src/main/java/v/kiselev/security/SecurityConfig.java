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
        auth.inMemoryAuthentication().withUser("mem_guest")
                .password(passEncoder.encode("123"))
                .roles("GUEST")
                .and().withUser("admin")
                .password(passEncoder.encode("123"))
                .roles("ADMIN");

//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userAuthService);
//        provider.setPasswordEncoder(passEncoder);
//
//        auth.authenticationProvider(provider);
    }

//    @Configuration
//    public static class UiWebSecurityConfigAdapter extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.
//                    authorizeRequests()
//                    .antMatchers("/**/*.css", "/**/*.js").permitAll()
//                    .antMatchers("/product/**").permitAll()
//                    .antMatchers("/user/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
//                    .and()
//                    .formLogin()
//                    .loginPage("/login")
//                    .defaultSuccessUrl("/user")
//                    .and()
//                    .exceptionHandling()
//                    .accessDeniedPage("/access_denied");
//        }
//    }
}
