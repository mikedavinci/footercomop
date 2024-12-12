package wi.roger.rogerWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class ValidationResultDto {
    private boolean valid;
    private List<String> errors;
}