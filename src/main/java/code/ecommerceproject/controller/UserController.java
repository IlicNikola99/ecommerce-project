package code.ecommerceproject.controller;

import code.ecommerceproject.dto.UserDto;
import code.ecommerceproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("public/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/authenticated")
    public UserDto getUserFromSession(final HttpSession session) {
        final UserDto user = (UserDto) session.getAttribute("user");
        if (user == null) {
            return new UserDto();
        }
        return user;
    }

    @GetMapping("/{email}")
    public UserDto getUserByEmail(final @PathVariable String email) {
        return userService.findUserByEmail(email);
    }
}
