package wi.inspire.InspireWI.DTO.School;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
public class SchoolRequestDto {
    @NotBlank(message = "School name is required")
    private String name;

    @NotNull(message = "Grade level is required")
    private CommonEnums.GradeLevel gradeLevel;

    @NotNull(message = "Access type is required")
    private CommonEnums.AccessTo accessTo;

    private String districtName;

    private CommonEnums.County county;

    @Builder.Default
    private Set<CommonEnums.County> activityRegions = new HashSet<>();

    @Email(message = "Please provide a valid counselor email address")
    private String counselorEmail;

    private String dashboardLink;

    @Email(message = "Please provide a valid Inspire email address")
    private String inspireEmail;

    @Email(message = "Please provide a valid permission email address")
    private String permissionEmail;

    private String notes;

    private Boolean isM7Educator;
}

