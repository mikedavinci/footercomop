// /DTO/User/UserListResponseDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.DTO.School.SchoolReferenceDto;
import wi.roger.rogerWI.types.CommonEnums;
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
    private Set<CommonEnums.County> servingCounties;
    private String deployment;
    private String title;
    private CommonEnums.EducatorRole educatorRole;
    private CommonEnums.GradeLevel gradeLevel;
}