package wi.inspire.InspireWI.DTO.Activity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.inspire.InspireWI.types.CommonEnums;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityRequestDto {
    private UUID requestorId;
    private UUID  activityId;
    @Builder.Default
    private Set<UUID> companyIds = new HashSet<>();
    private LocalDate preferredDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer numberOfParticipants;
    @Builder.Default
    private Set<CommonEnums.GradeLevel> participantGrades = new HashSet<>();
    private String objectives;
    private String subjectAlignment;
    @Builder.Default
    private Set<CommonEnums.AVRequirement> avRequirements = new HashSet<>();
    @Builder.Default
    private Boolean waiverAccepted = false;
    @Builder.Default
    private Boolean employerNotificationAccepted = false;
    private String speakerTopics;
    private LocalDate projectStartDate;
    private Integer projectDuration;
    private String projectCurriculum;
    private Integer numberOfVolunteers;
    private String volunteerNeedsDescription;
    private String mentorshipGoals;
    private Integer numberOfMentorSessions;
    private String careerInterestArea;
    private String sessionQuestions;
    private String arrivalInstructions;
}