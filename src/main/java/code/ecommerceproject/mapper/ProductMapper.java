package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.CategoryDto;
import code.ecommerceproject.dto.PictureDto;
import code.ecommerceproject.dto.ProductDto;
import code.ecommerceproject.entity.Category;
import code.ecommerceproject.entity.Picture;
import code.ecommerceproject.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper Instance = Mappers.getMapper(ProductMapper.class);

    Product toEntity(ProductDto productDto);

    @Mapping(target = "category", source = "category", qualifiedByName = "mapCategoryToDto")
    @Mapping(target = "pictures", source = "pictures", qualifiedByName = "mapPicturesToDto")
    ProductDto toDto(Product product);

    @Named("mapCategoryToDto")
    default CategoryDto mapCategoryToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    @Named("mapPicturesToDto")
    default List<PictureDto> mapPicturesToDto(Set<Picture> pictures) {
        return pictures.stream()
                .map(picture -> {
                    PictureDto pictureDto = new PictureDto();
                    pictureDto.setId(picture.getId());
                    pictureDto.setUrl(picture.getUrl());
                    return pictureDto;
                })
                .collect(Collectors.toList());
    }
}