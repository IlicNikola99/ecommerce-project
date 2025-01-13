package code.ecommerceproject.service;

import code.ecommerceproject.dto.UserDto;
import code.ecommerceproject.entity.User;
import code.ecommerceproject.mapper.UserMapper;
import code.ecommerceproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto findUserByEmail(String email) {
        final Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return UserMapper.Instance.toDto(user.get());
        } else throw new NotFoundException("User not found");
    }


}
