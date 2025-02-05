package code.ecommerceproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.UUID;

@Data
public class PictureDto {

    private UUID id;
    private String url;
    @JsonIgnore
    private ProductDto product;
}