package wi.inspire.InspireWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.DTO.Activity.ActivityRecordResponseDto;
import wi.inspire.InspireWI.DTO.Activity.ActivityRequestResponseDto;
import wi.inspire.InspireWI.DTO.User.UserResponseDto;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class QuestionnaireResponseDto {
    private UUID id;
    private UserResponseDto user;
    private ActivityRequestResponseDto activityRequest;
    private ActivityRecordResponseDto activityRecord;
    private LocalDateTime submittedDate;
    private String imageData;
}

// For enhanced version of questionnaire submission
//public class QuestionnaireResponseDto {
//    private UUID id;
//    private UUID userId;
//    private Map<String, Object> responses;
//    private UUID activityRequestId;
//    private UUID activityRecordId;
//    private LocalDateTime submittedDate;
//}