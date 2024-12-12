// /DTO/User/UserDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;
import javax.validation.constraints.*;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private CommonEnums.UserType userType;
    private String phoneNumber;
    private String workPhone;
    private CommonEnums.PhoneType phoneType;
    private Boolean smsConsent;
    private String parentGuardianEmail;
    private UUID schoolId;
    private String schoolName;
}