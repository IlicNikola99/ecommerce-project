package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.PictureDto;
import code.ecommerceproject.entity.Picture;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    @Mapping(source = "product.id", target = "productId")
    PictureDto toDTO(Picture picture);

    @Mapping(source = "productId", target = "product.id")
    Picture toEntity(PictureDto pictureDTO);
}