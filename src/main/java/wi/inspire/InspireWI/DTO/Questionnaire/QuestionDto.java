package wi.inspire.InspireWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class QuestionDto {
    private UUID id;
    private String text;
    private CommonEnums.QuestionType type;
    private List<OptionDto> options;
    private Map<String, Object> dependencies;
    private boolean required;
    private String validationMessage;
}