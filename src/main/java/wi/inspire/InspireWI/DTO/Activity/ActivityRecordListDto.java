package wi.roger.rogerWI.DTO.Activity;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.DTO.Company.CompanyReferenceDto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityRecordListDto {
    private UUID id;
    private ActivityReferenceDto activity;
    private LocalDate activityDate;
    private Integer numberOfParticipants;
    @Builder.Default
    private Set<CompanyReferenceDto> participatingCompanies = new HashSet<>();
}
