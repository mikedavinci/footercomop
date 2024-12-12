// /DTO/User/EducatorRequestDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import javax.validation.constraints.*;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EducatorRequestDto extends UserRequestDto {
    private CommonEnums.EducatorRole educatorRole;
    private String customRole;
    private CommonEnums.GradeLevel gradeLevel;
    @Override
    public UUID getSchoolId() {
        return super.getSchoolId();
    }
}