package code.ecommerceproject.service;

import code.ecommerceproject.dto.UserDto;
import code.ecommerceproject.entity.User;
import code.ecommerceproject.mapper.UserMapper;
import code.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = delegate.loadUser(userRequest);

        final String client = userRequest.getClientRegistration().getRegistrationId();

        if (client.equals("google")) {
            handleGoogleUser(oAuth2User);
        } else if (client.equals("github")) {
            handleGithubUser(oAuth2User);
        }

        return new DefaultOAuth2User(
                oAuth2User.getAuthorities(),
                oAuth2User.getAttributes(),
                "name"
        );
    }

    private void handleGoogleUser(final OAuth2User oAuth2User) {
        final UserDto userInfo = getGoogleUserInfo(oAuth2User);

        userRepository.findByEmail(userInfo.getEmail()).orElseGet(() -> {
            final User newUser = UserMapper.Instance.toEntity(userInfo);
            newUser.setRole("ROLE_USER");
            return userRepository.save(newUser);
        });
    }

    private void handleGithubUser(final OAuth2User oAuth2User) {
        final UserDto userInfo = getGithubUserInfo(oAuth2User);

        userRepository.findByOauthId(userInfo.getOauthId()).orElseGet(() -> {
            final User newUser = UserMapper.Instance.toEntity(userInfo);
            newUser.setRole("ROLE_USER");
            return userRepository.save(newUser);
        });
    }

    private UserDto getGoogleUserInfo(final OAuth2User oAuth2User) {
        final UserDto userDto = new UserDto();
        userDto.setEmail(oAuth2User.getAttribute("email"));
        userDto.setFirstName(oAuth2User.getAttribute("given_name"));
        userDto.setLastName(oAuth2User.getAttribute("family_name"));
        userDto.setProfilePictureLink(oAuth2User.getAttribute("picture"));
        return userDto;
    }

    private UserDto getGithubUserInfo(final OAuth2User oAuth2User) {
        final UserDto userDto = new UserDto();
        final String fullName = oAuth2User.getAttribute("name");

        userDto.setEmail(oAuth2User.getAttribute("email"));
        userDto.setUsername(oAuth2User.getAttribute("login"));
        userDto.setOauthId(oAuth2User.getAttribute("id"));

        if (fullName != null && fullName.split(" ").length > 1) {
            userDto.setFirstName(fullName.split(" ")[0]);
            userDto.setLastName(fullName.split(" ")[1]);
        }

        return userDto;
    }
}