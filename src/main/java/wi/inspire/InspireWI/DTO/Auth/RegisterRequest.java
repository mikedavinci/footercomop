package wi.roger.rogerWI.DTO.Auth;

import lombok.Data;
import wi.roger.rogerWI.types.CommonEnums.UserType;
import wi.roger.rogerWI.types.CommonEnums.GradeLevel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class RegisterRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "User type is required")
    private UserType userType;

    private String phoneNumber;

    private UUID schoolId;

    private GradeLevel gradeLevel;
}