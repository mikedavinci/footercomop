package wi.inspire.InspireWI.DTO.Company;
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
public class CompanyListDto {
    private UUID id;
    private String name;
    @Builder.Default
    private Set<CommonEnums.County> regions = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.Cluster> clusters = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.ActivityCategory> activities = new HashSet<>();
    private String contactName;
}