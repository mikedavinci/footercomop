// /DTO/User/StudentResponseDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums;

import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentResponseDto extends UserResponseDto {
    private String studentId;
    private String intendedMajor;
    private CommonEnums.GradeLevel gradeLevel;
    private LocalDate dateOfBirth;
}