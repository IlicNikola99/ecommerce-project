package code.ecommerceproject.controller;

import code.ecommerceproject.dto.UserDto;
import code.ecommerceproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/authenticated")
    public UserDto user(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return new UserDto();
        }

        return userService.findUserByEmail(principal.getAttribute("email"));
    }
}
