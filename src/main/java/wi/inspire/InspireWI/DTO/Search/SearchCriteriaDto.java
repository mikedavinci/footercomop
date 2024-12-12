package wi.roger.rogerWI.DTO.Search;

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
public class SearchCriteriaDto {
    private String query;
    @Builder.Default
    private Set<CommonEnums.Cluster> clusters = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.County> regions = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.ActivityCategory> categories = new HashSet<>();
    @Builder.Default
    private Set<CommonEnums.UserType> userTypes = new HashSet<>();
    private CommonEnums.DeliveryMode deliveryMode;
    private Boolean isPrescheduled;
    private String email;
    private String name;
}
