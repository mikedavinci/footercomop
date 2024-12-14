package wi.roger.rogerWI.DTO.Auth;

import lombok.Data;
import wi.roger.rogerWI.types.CommonEnums.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Data
public class RegistrationRequest {
    @NotBlank(message = "Name is required")
    private String name;
    @Email(message = "Please provide a valid email address")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotNull(message = "User type is required")
    private UserType userType;

    // Common fields
    private String phoneNumber;
    private String workPhone;
    private PhoneType phoneType;
    private Boolean smsConsent;
    private String parentGuardianEmail;
    private UUID schoolId;

    // Regional Lead specific fields
    private String deployment;
    private String title;
    private Set<County> servingCounties;

    // Student specific fields
    private String studentId;
    private String intendedMajor;
    private GradeLevel gradeLevel;
    private LocalDate dateOfBirth;

    // Educator specific fields
    private EducatorRole educatorRole;
    private String customRole;

    // Coordinator specific fields
    private Set<UUID> assignedSchoolIds;
}