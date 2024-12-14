package wi.roger.rogerWI.DTO.User.Auth;

import lombok.Data;

@Data
public class PasswordUpdateDto {
    private String currentPassword;
    private String newPassword;
}