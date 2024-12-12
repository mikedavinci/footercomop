package wi.inspire.InspireWI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import wi.inspire.InspireWI.types.CommonEnums;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "companies")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @NotBlank(message = "Company name is required")
    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(
            name = "company_regions",
            joinColumns = @JoinColumn(name = "company_id")
    )
    @Column(name = "county")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.County> regions = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "company_career_clusters",
            joinColumns = @JoinColumn(name = "company_id")
    )
    @Column(name = "cluster")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.Cluster> clusters = new HashSet<>();

    @ElementCollection
    @CollectionTable(
            name = "company_activities",
            joinColumns = @JoinColumn(name = "company_id")
    )
    @Column(name = "activity_category")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.ActivityCategory> activities = new HashSet<>();

    @Column(name = "contact_name")
    private String contactName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Please provide a valid contact phone number")
    @Column(name = "contact_phone")
    private String contactPhone;

    @Email(message = "Please provide a valid contact email address")
    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "dashboard_link")
    private String dashboardLink;

    @Column(name = "areas_of_interest")
    private String areasOfInterest;

    @Enumerated(EnumType.STRING)
    @Column(name = "tour_type")
    private CommonEnums.TourType tourType;

    @ManyToMany(mappedBy = "companies", fetch = FetchType.LAZY)
    private Set<ActivityRequest> activityRequests = new HashSet<>();

    @ManyToMany(mappedBy = "participatingCompanies", fetch = FetchType.LAZY)
    private Set<ActivityRecord> activityRecords = new HashSet<>();

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}