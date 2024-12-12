package wi.roger.rogerWI.DTO.Activity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.DTO.Career.CareerReferenceDto;
import wi.roger.rogerWI.DTO.Company.CompanyReferenceDto;
import wi.roger.rogerWI.DTO.User.UserResponseDto;
import wi.roger.rogerWI.types.CommonEnums;

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