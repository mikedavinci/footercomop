package wi.inspire.InspireWI.DTO.Activity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.DTO.Career.CareerReferenceDto;
import wi.inspire.InspireWI.types.CommonEnums;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityResponseDto {
    private UUID id;
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
    private Set<CareerReferenceDto> availablePathways = new HashSet<>();
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}