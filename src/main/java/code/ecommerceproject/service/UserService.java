package code.ecommerceproject.service;

import code.ecommerceproject.entity.User;
import code.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserByEmail(String email) {
        final Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else throw new NotFoundException("User not found");
    }

    /**
     * Returns true if the authenticated user is an admin or matches the provided userId.
     */
    public boolean checkUser(UUID userId, Authentication authentication) {
        final String email = (String) authentication.getPrincipal();
        final User loggedInUser = findUserByEmail(email);

        final boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority()));

        return isAdmin || loggedInUser.getId().equals(userId);
    }


}
