// /DTO/User/UserRequestDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums;

import javax.validation.constraints.*;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserRequestDto {
    private String name;
    private String email;
    private CommonEnums.UserType userType;
    private String phoneNumber;
    private String workPhone;
    private CommonEnums.PhoneType phoneType;
    private Boolean smsConsent;
    private String parentGuardianEmail;
    private UUID schoolId;
}