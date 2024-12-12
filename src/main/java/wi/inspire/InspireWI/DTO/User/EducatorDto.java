// /DTO/User/EducatorDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EducatorDto extends UserDto {
    private CommonEnums.EducatorRole educatorRole;
    private String customRole;
    private CommonEnums.GradeLevel gradeLevel;
}