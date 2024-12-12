// /DTO/User/StudentDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;
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