package wi.roger.rogerWI.DTO.User;

import lombok.*;
import wi.roger.rogerWI.types.CommonEnums.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class UserRegistrationDto {
    private String name;
    private String email;
    private String password;
    private UserType userType;
    private String phoneNumber;
    private String workPhone;
    private PhoneType phoneType;
    private Boolean smsConsent;
    private String parentGuardianEmail;
    private Set<County> servingCounties;
    private String deployment;
    private String title;
    private String studentId;
    private String intendedMajor;
    private GradeLevel gradeLevel;
    private LocalDate dateOfBirth;
    private EducatorRole educatorRole;
    private String customRole;
}
