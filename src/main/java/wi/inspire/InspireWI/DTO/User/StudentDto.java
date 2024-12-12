// /DTO/User/StudentDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums;
import java.time.LocalDate;

@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentDto extends UserDto {
    private String studentId;
    private String intendedMajor;
    private CommonEnums.GradeLevel gradeLevel;
    private LocalDate dateOfBirth;
}