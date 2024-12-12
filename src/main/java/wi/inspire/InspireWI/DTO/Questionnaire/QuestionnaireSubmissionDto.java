package wi.roger.rogerWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.util.Map;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class QuestionnaireSubmissionDto {
    private UUID sessionId;
    private Map<String, Object> responses;
    private String captchaToken;
}