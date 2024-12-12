// /DTO/User/CoordinatorRequestDto.java
package wi.inspire.InspireWI.DTO.User;

import lombok.Data;
import lombok.Builder;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums.County;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CoordinatorRequestDto extends UserRequestDto {
    @Builder.Default
    private Set<County> servingCounties = new HashSet<>();
    @Builder.Default
    private Set<UUID> assignedSchoolIds = new HashSet<>();
}