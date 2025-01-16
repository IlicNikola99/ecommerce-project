package code.ecommerceproject.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PictureDto {

    private UUID id;

    private String mimeType;

    private UUID productId;
}