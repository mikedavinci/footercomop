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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "activity_records")
public class ActivityRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recorder_id", nullable = false)
    private User recorder;

    @Column(name = "activity_date", nullable = false)
    private LocalDate activityDate;

    @Column(name = "number_of_participants", nullable = false)
    private Integer numberOfParticipants;

    @ElementCollection
    @CollectionTable(
            name = "activity_record_grades",
            joinColumns = @JoinColumn(name = "activity_record_id")
    )
    @Column(name = "grade_level")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.GradeLevel> participantGrades = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_record_pathways",
            joinColumns = @JoinColumn(name = "activity_record_id"),
            inverseJoinColumns = @JoinColumn(name = "career_pathway_id")
    )
    private Set<CareerPathway> careerPathways = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_record_companies",
            joinColumns = @JoinColumn(name = "activity_record_id"),
            inverseJoinColumns = @JoinColumn(name = "company_id")
    )
    private Set<Company> participatingCompanies = new HashSet<>();

    @Column(name = "documentation_url")
    private String documentationUrl;

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}