package wi.inspire.InspireWI.DTO.School;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class SchoolResponseDto {
    private UUID id;
    private String name;
    private CommonEnums.GradeLevel gradeLevel;
    private CommonEnums.AccessTo accessTo;
    private String districtName;
    private CommonEnums.County county;
    @Builder.Default
    private Set<CommonEnums.County> activityRegions = new HashSet<>();
    private String counselorEmail;
    private String dashboardLink;
    private String inspireEmail;
    private String permissionEmail;
    private String notes;
    private Boolean isM7Educator;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}