package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.ProductDto;
import code.ecommerceproject.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductMapper Instance = Mappers.getMapper(ProductMapper.class);

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);
}