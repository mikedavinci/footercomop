package wi.roger.rogerWI.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import wi.roger.rogerWI.types.CommonEnums;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "activity_requests")
public class ActivityRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToMany
    @JoinTable(
            name = "activity_request_companies",
            joinColumns = @JoinColumn(name = "activity_request_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> companies = new HashSet<>();

    @Column(name = "preferred_date")
    private LocalDate preferredDate;

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    @ElementCollection
    @CollectionTable(
            name = "activity_request_grades",
            joinColumns = @JoinColumn(name = "activity_request_id")
    )
    @Column(name = "grade_level")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.GradeLevel> participantGrades = new HashSet<>();

    @Column(columnDefinition = "TEXT")
    private String objectives;

    @Column(name = "subject_alignment")
    private String subjectAlignment;

    @ElementCollection
    @CollectionTable(
            name = "activity_request_av_requirements",
            joinColumns = @JoinColumn(name = "activity_request_id")
    )
    @Column(name = "av_requirement")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.AVRequirement> avRequirements = new HashSet<>();

    @Column(name = "waiver_accepted")
    private Boolean waiverAccepted = false;

    @Column(name = "employer_notification_accepted")
    private Boolean employerNotificationAccepted = false;

    @Column(name = "speaker_topics", columnDefinition = "TEXT")
    private String speakerTopics;

    @Column(name = "project_start_date")
    private LocalDate projectStartDate;

    @Column(name = "project_duration")
    private Integer projectDuration;

    @Column(name = "project_curriculum", columnDefinition = "TEXT")
    private String projectCurriculum;

    @Min(value = 0, message = "Number of volunteers cannot be negative")
    @Column(name = "number_of_volunteers")
    private Integer numberOfVolunteers;

    @Column(name = "volunteer_needs_description", columnDefinition = "TEXT")
    private String volunteerNeedsDescription;

    @Column(name = "mentorship_goals", columnDefinition = "TEXT")
    private String mentorshipGoals;

    @Min(value = 0, message = "Number of mentor sessions cannot be negative")
    @Column(name = "number_of_mentor_sessions")
    private Integer numberOfMentorSessions;

    @Column(name = "career_interest_area")
    private String careerInterestArea;

    @Column(name = "session_questions", columnDefinition = "TEXT")
    private String sessionQuestions;

    @Column(name = "arrival_instructions", columnDefinition = "TEXT")
    private String arrivalInstructions;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}