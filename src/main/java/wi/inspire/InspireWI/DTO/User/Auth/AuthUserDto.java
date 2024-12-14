package wi.roger.rogerWI.DTO.User.Auth;

import lombok.*;
import wi.roger.rogerWI.types.CommonEnums.*;

import java.util.UUID;

@Data
@Builder
public class AuthUserDto {
    private UUID id;
    private String name;
    private String email;
    private UserType userType;
}