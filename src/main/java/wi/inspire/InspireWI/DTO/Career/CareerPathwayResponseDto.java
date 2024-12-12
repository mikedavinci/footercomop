package wi.inspire.InspireWI.DTO.Career;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import wi.inspire.InspireWI.types.CommonEnums.*;

@Data
@SuperBuilder
@NoArgsConstructor
public class CareerPathwayResponseDto {
    private UUID id;
    private String name;
    private Cluster cluster;
    @Builder.Default
    private Set<CareerReferenceDto> careers = new HashSet<>();
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}