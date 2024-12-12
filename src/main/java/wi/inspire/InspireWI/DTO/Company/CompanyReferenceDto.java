package wi.inspire.InspireWI.DTO.Company;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class CompanyReferenceDto {
    private UUID id;
    private String name;
}