package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.UserDto;
import code.ecommerceproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper Instance = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User toEntity(UserDto userDTO);
}