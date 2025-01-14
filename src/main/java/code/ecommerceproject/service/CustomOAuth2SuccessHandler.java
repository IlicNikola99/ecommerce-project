package code.ecommerceproject.service;

import code.ecommerceproject.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserService userService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        final OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        final OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(
                        oauthToken.getAuthorizedClientRegistrationId(),
                        oauthToken.getName());

        final String accessToken = client.getAccessToken().getTokenValue();
        final DefaultOAuth2User principal = (DefaultOAuth2User) oauthToken.getPrincipal();
        final UserDto userDto = userService.findUserByEmail(principal.getAttribute("email"));

        final HttpSession session = request.getSession();

        session.setAttribute("accessToken", accessToken);
        session.setAttribute("user", userDto);

        final String frontendRedirectUrl = "http://localhost:4200/login-success?token="
                + accessToken + "&email=" + URLEncoder.encode(userDto.getEmail(), StandardCharsets.UTF_8); //TODO replace this hardcode
        response.sendRedirect(frontendRedirectUrl);
    }
}