package code.ecommerceproject.config;

import code.ecommerceproject.service.CustomOAuth2SuccessHandler;
import code.ecommerceproject.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfiguration(CustomOAuth2UserService customOAuth2UserService, CustomOAuth2SuccessHandler customOAuth2SuccessHandler, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.customOAuth2SuccessHandler = customOAuth2SuccessHandler;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/public/**").permitAll() // Permit access to public paths
                        .requestMatchers("/api/product/featured").permitAll()
                        .requestMatchers("/api/product/related").permitAll()
                        .requestMatchers("/api/product/{id}").permitAll()
                        .requestMatchers("/api/product/search").permitAll()
                        .requestMatchers("/api/category/findAll").permitAll()
                        .requestMatchers("/api/orders/get-cart-details").permitAll()
                        .requestMatchers("/api/orders/webhook").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .logout(l -> l
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/").permitAll()
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .deleteCookies("JSESSIONID")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // Log the successful logout information
                            System.out.println("Successfully logged out");
                            response.setStatus(200); // Ensure the response status is 200
                            response.getWriter().write("Logout successful"); // Optional: Write success message
                        })
                )

                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))  // Handle unauthorized access
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)
                        )
                        .failureUrl("http://localhost:4200/login-failure") // Redirect to Angular app on failure
                        .successHandler(customOAuth2SuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            // Log the exception for debugging
                            System.err.println("Authentication failed: " + exception.getMessage());

                            // Respond with a failure message
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Authentication failed\", \"message\": \""
                                    + exception.getMessage() + "\"}");
                        })

                );

        return http.build();
    }
}