package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.CategoryDto;
import code.ecommerceproject.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryMapper Instance = Mappers.getMapper(CategoryMapper.class);

    @Mapping(target = "products", ignore = true)
    CategoryDto toDto(Category user);

    Category toEntity(CategoryDto userDTO);
}