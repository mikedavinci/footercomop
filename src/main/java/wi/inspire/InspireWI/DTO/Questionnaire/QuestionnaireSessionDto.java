package wi.inspire.InspireWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class QuestionnaireSessionDto {
    private UUID sessionId;
    private String currentStep;
    private List<QuestionDto> nextQuestions;
    private boolean completed;
    private LocalDateTime lastUpdated;
}