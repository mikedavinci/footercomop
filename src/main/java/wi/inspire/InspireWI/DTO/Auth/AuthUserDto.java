package wi.roger.rogerWI.DTO.Auth;

import lombok.Builder;
import lombok.Data;
import wi.roger.rogerWI.types.CommonEnums.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Builder
public class AuthUserDto {
    private UUID id;
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Please provide a valid email address")
    private String email;
    private UserType userType;
}