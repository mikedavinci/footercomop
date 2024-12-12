package wi.inspire.InspireWI.DTO.Activity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityListDto {
    private UUID id;
    private String name;
    private CommonEnums.AccessLevel accessLevel;
    @Builder.Default
    private Set<CommonEnums.AccessType> accessTypes = new HashSet<>();
    private CommonEnums.ActivityCategory category;
    private CommonEnums.DeliveryMode deliveryMode;
    private Boolean isPrescheduled;
}
