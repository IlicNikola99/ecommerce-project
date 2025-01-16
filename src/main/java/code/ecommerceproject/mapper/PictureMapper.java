package code.ecommerceproject.mapper;

import code.ecommerceproject.dto.PictureDto;
import code.ecommerceproject.entity.Picture;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    PictureDto toDTO(Picture picture);

    Picture toEntity(PictureDto pictureDTO);
}