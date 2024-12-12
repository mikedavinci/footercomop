package wi.roger.rogerWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ValidationErrorDto {
    private UUID questionId;
    private String message;
    private String code;
}