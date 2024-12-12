package wi.roger.rogerWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
public class QuestionnaireErrorResponse {
    private String message;
    private String code;
    private List<ValidationErrorDto> validationErrors;
    private LocalDateTime timestamp;
}