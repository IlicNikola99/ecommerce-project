package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.CategoryDto;
import code.ecommerceproject.dto.ProductDto;
import code.ecommerceproject.entity.Category;
import code.ecommerceproject.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper Instance = Mappers.getMapper(ProductMapper.class);

    Product toEntity(ProductDto productDto);

    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToDto")
    @Mapping(target = "pictures", ignore = true)
    ProductDto toDto(Product product);

    @Named("mapCategoryToDto")
    default CategoryDto mapCategoryToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }
}