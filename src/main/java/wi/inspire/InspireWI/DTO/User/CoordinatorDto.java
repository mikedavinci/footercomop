// /DTO/User/CoordinatorDto.java
package wi.roger.rogerWI.DTO.User;

import lombok.Data;
import lombok.Builder;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.DTO.School.SchoolReferenceDto;
import wi.roger.rogerWI.types.CommonEnums.County;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CoordinatorDto extends UserDto {
    @Builder.Default
    private Set<County> servingCounties = new HashSet<>();
    @Builder.Default
    private Set<SchoolReferenceDto> assignedSchools = new HashSet<>();
}