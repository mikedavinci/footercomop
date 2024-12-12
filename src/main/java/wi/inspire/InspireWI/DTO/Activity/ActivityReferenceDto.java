package wi.roger.rogerWI.DTO.Activity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.types.CommonEnums;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityReferenceDto {
    private UUID id;
    private String name;
    private CommonEnums.ActivityCategory category;
}