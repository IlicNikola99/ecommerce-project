package code.ecommerceproject.config;

import code.ecommerceproject.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfiguration(CustomOAuth2UserService customOAuth2UserService) {
        this.customOAuth2UserService = customOAuth2UserService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )
                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers("/logout")
                )
                .logout(l -> l
                        .logoutSuccessUrl("/").permitAll()
                )

                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))  // Handle unauthorized access
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .defaultSuccessUrl("http://localhost:4200", true) // Redirect to Angular app after login
                        .failureUrl("http://localhost:4200/login-failure") // Redirect to Angular app on failure
                        .failureHandler((request, response, exception) -> {
                            // Log the exception for debugging
                            System.err.println("Authentication failed: " + exception.getMessage());

                            // Optionally log more details
                            exception.printStackTrace();

                            // Respond with a failure message
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Authentication failed\", \"message\": \""
                                    + exception.getMessage() + "\"}");

                            // If you want to redirect to a specific page (optional):
                            // response.sendRedirect("http://localhost:4200/login-failure");
                        })

                );

        return http.build();
    }
}