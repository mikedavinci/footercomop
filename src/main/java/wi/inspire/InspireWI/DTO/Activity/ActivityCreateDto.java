package wi.roger.rogerWI.DTO.Activity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityCreateDto {
    private String name;
    private CommonEnums.AccessLevel accessLevel;
    @Builder.Default
    private Set<CommonEnums.AccessType> accessTypes = new HashSet<>();
    private CommonEnums.ActivityType activityType;
    private CommonEnums.DeliveryMode deliveryMode;
    private CommonEnums.ActivityCategory category;
    private String description;
    private Boolean isPrescheduled;
    private Boolean multipleCompanyAllowed;
    @Builder.Default
    private Set<UUID> pathwayIds = new HashSet<>();
}
