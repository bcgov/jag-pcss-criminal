package ca.bc.gov.open.pcsscriminalapplication.configuration;

import ca.bc.gov.open.pcsscriminalapplication.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final SecurityProperties securityProperties;
    private MyBasicAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    public SecurityConfig(
            SecurityProperties securityProperties,
            MyBasicAuthenticationEntryPoint authenticationEntryPoint) {
        this.securityProperties = securityProperties;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(new AntPathRequestMatcher("/error"))
                                .permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/actuator/**"))
                                .permitAll()
                                .anyRequest()
                                .authenticated());

        http.sessionManagement(
                httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS);
                });

        http.httpBasic(
                httpSecurityHttpBasicConfigurer -> {
                    httpSecurityHttpBasicConfigurer.authenticationEntryPoint(
                            authenticationEntryPoint);
                });

        http.csrf(
                httpSecurityCsrfConfigurer -> {
                    httpSecurityCsrfConfigurer.disable();
                });

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user =
                User.builder()
                        .username(securityProperties.getUsername())
                        .password(passwordEncoder().encode(securityProperties.getPassword()))
                        .roles("Admin")
                        .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword);
            }
        };
    }
}
