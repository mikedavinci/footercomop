package wi.roger.rogerWI.DTO.Company;

import lombok.Builder;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums;
import java.util.HashSet;
import java.util.Set;

@Data
@SuperBuilder
@NoArgsConstructor
public class CompanyRequestDto {
    private String name;
    @Builder.Default
    private Set<CommonEnums.County> regions = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.Cluster> clusters = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.ActivityCategory> activities = new HashSet<>();
    private String contactName;
    private String contactPhone;
    private String contactEmail;
    private String dashboardLink;
    private String areasOfInterest;
    private CommonEnums.TourType tourType;
}