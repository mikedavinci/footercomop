package wi.inspire.InspireWI.DTO.School;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class SchoolReferenceDto {
    private UUID id;
    private String name;
}