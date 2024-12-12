// /DTO/User/UserResponseDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.DTO.School.SchoolReferenceDto;
import wi.roger.rogerWI.types.CommonEnums;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private CommonEnums.UserType userType;
    private String phoneNumber;
    private String workPhone;
    private CommonEnums.PhoneType phoneType;
    private Boolean smsConsent;
    private String parentGuardianEmail;
    private SchoolReferenceDto school;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
