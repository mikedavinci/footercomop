package wi.inspire.InspireWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class OptionDto {
    private String value;
    private String label;
    private boolean disabled;

    public OptionDto(String value, String label) {
        this.value = value;
        this.label = label;
        this.disabled = false;
    }
}