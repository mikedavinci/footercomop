package wi.roger.rogerWI.DTO.Activity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import wi.roger.rogerWI.DTO.Company.CompanyReferenceDto;
import wi.roger.rogerWI.DTO.User.UserResponseDto;
import wi.roger.rogerWI.types.CommonEnums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class ActivityRequestResponseDto {
    private UUID id;
    private UserResponseDto requestor;
    private ActivityReferenceDto activity;
    @Builder.Default
    private Set<CompanyReferenceDto> companies = new HashSet<>();

    private LocalDate preferredDate;
    private LocalDate completionDate;
    private LocalTime startTime;
    private LocalTime endTime;

    private Integer numberOfParticipants;
    @Builder.Default
    private Set<CommonEnums.GradeLevel> participantGrades = new HashSet<>();

    private String objectives;
    private String subjectAlignment;
    @Builder.Default
    private Set<CommonEnums.AVRequirement> avRequirements = new HashSet<>();

    private Boolean waiverAccepted;
    private Boolean employerNotificationAccepted;

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

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
