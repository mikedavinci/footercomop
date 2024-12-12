package wi.inspire.InspireWI.DTO.Activity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityRecordDto {
    private UUID recorderId;
    private UUID activityId;
    private LocalDate activityDate;
    private Integer numberOfParticipants;
    @Builder.Default
    private Set<CommonEnums.GradeLevel> participantGrades = new HashSet<>();
    @Builder.Default
    private Set<UUID> careerPathwayIds = new HashSet<>();
    @Builder.Default
    private Set<UUID> participatingCompanyIds = new HashSet<>();
    private String documentationUrl;
}