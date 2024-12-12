package wi.roger.rogerWI.DTO.Career;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class CareerPathwayRequestDto {
    private String name;
    private Cluster cluster;
    @Builder.Default
    private Set<UUID> careerIds = new HashSet<>();
}
