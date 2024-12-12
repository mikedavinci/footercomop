// /DTO/User/UserListResponseDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.Data;
import lombok.Builder;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.DTO.School.SchoolReferenceDto;
import wi.inspire.InspireWI.types.CommonEnums;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserListResponseDto {
    private UUID id;
    private String name;
    private String email;
    private CommonEnums.UserType userType;
    private SchoolReferenceDto school;
    @Builder.Default
    private Set<CommonEnums.County> servingCounties = new HashSet<>();
    private String deployment;
    private String title;
    private CommonEnums.EducatorRole educatorRole;
    private CommonEnums.GradeLevel gradeLevel;
}