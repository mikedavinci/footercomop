package wi.inspire.InspireWI.DTO.Activity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.DTO.Career.CareerReferenceDto;
import wi.inspire.InspireWI.DTO.Company.CompanyReferenceDto;
import wi.inspire.InspireWI.DTO.User.UserResponseDto;
import wi.inspire.InspireWI.types.CommonEnums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityRecordResponseDto {
    private UUID id;
    private ActivityReferenceDto activity;
    private UserResponseDto recorder;
    private LocalDate activityDate;
    private Integer numberOfParticipants;
    @Builder.Default
    private Set<CommonEnums.GradeLevel> participantGrades = new HashSet<>();
    @Builder.Default
    private Set<CareerReferenceDto> careerPathways = new HashSet<>();
    @Builder.Default
    private Set<CompanyReferenceDto> participatingCompanies = new HashSet<>();
    private String documentationUrl;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}