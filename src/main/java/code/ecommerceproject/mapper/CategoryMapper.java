package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.CategoryDto;
import code.ecommerceproject.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper Instance = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category user);

    Category toEntity(CategoryDto userDTO);
}