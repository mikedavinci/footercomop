package wi.inspire.InspireWI.DTO.Questionnaire;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class FormSubmissionDto {
    private String userType;  // STUDENT or EDUCATOR
    private String name;
    private String email;
    private String phoneNumber;
    private String grade;  // For students
    private UUID schoolId;
    private String activityType;  // REQUEST or RECORD
    private LocalDate preferredDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer numberOfParticipants;
    private Set<String> participantGrades;
    private String objectives;
    private Boolean waiverAccepted;
    private String imageData;

    // Additional fields based on activity type
    // private Map<String, Object> additionalFields;
    // For any extra dynamic fields
}
