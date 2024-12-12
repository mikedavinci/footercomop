package wi.inspire.InspireWI.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import wi.inspire.InspireWI.types.CommonEnums;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "activity_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommonEnums.ActivityType activityType;

    @Column(name = "delivery_mode", nullable = false)
    @Enumerated(EnumType.STRING)
    private CommonEnums.DeliveryMode deliveryMode;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommonEnums.ActivityCategory category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_prescheduled")
    private Boolean isPrescheduled = false;

    @Column(name = "multiple_company_allowed")
    private Boolean multipleCompanyAllowed = false;

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<ActivityRequest> activityRequests = new HashSet<>();

    @OneToMany(mappedBy = "activity", fetch = FetchType.LAZY)
    private Set<ActivityRecord> activityRecords = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level")
    private CommonEnums.AccessLevel accessLevel;

    @ElementCollection
    @CollectionTable(
            name = "activity_access_types",
            joinColumns = @JoinColumn(name = "activity_id")
    )
    @Column(name = "access_type")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.AccessType> accessTypes = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "activity_pathways",
            joinColumns = @JoinColumn(name = "activity_id"),
            inverseJoinColumns = @JoinColumn(name = "career_pathway_id")
    )
    private Set<CareerPathway> availablePathways = new HashSet<>();

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}