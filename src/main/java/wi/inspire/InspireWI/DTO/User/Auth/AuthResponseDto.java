package wi.roger.rogerWI.DTO.User.Auth;

import lombok.*;

@Data
@Builder
public class AuthResponseDto {
    private String accessToken;
    @Builder.Default
    private String refreshToken = null;
    private AuthUserDto user;
}