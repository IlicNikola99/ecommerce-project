package code.ecommerceproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String profilePictureLink;
    private Integer oauthId;
    private String role;

}