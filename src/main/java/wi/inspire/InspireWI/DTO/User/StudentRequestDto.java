// /DTO/User/StudentRequestDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import java.time.LocalDate;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StudentRequestDto extends UserRequestDto {
    private String studentId;
    private String intendedMajor;
    private CommonEnums.GradeLevel gradeLevel;
    private LocalDate dateOfBirth;
    public UUID getSchoolId() {
        return super.getSchoolId();
    }
}