// /DTO/User/UserDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private UserType userType;
    private String deployment;
    private String title;
    private Set<County> servingCounties;
    private String phoneNumber;
    private String workPhone;
    private PhoneType phoneType;
    private Boolean smsConsent;
    private String parentGuardianEmail;
    private UUID schoolId;
    private String studentId;
    private String intendedMajor;
    private GradeLevel gradeLevel;
    private LocalDate dateOfBirth;
    private EducatorRole educatorRole;
    private String customRole;
}