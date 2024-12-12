package wi.inspire.InspireWI.DTO.Career;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class CareerReferenceDto {
    private UUID id;
    private String name;
}