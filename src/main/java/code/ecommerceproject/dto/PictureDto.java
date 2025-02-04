package code.ecommerceproject.dto;

import code.ecommerceproject.entity.Product;
import lombok.Data;

import java.util.UUID;

@Data
public class PictureDto {

    private UUID id;
    private String url;
    private Product product;
}