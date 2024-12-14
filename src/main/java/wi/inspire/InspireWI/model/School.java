package wi.roger.rogerWI.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import wi.roger.rogerWI.types.CommonEnums;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "schools")
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "grade_level")
    @Enumerated(EnumType.STRING)
    private CommonEnums.GradeLevel gradeLevel;

    @Column(name = "access_type")
    @Enumerated(EnumType.STRING)
    private CommonEnums.AccessTo accessTo = CommonEnums.AccessTo.FULL_SERVICE;

    @Column(name = "district_name")
    private String districtName;

    @Enumerated(EnumType.STRING)
    @Column(name = "county")
    private CommonEnums.County county;

    @ElementCollection
    @CollectionTable(
            name = "school_activity_regions",
            joinColumns = @JoinColumn(name = "school_id")
    )
    @Column(name = "county")
    @Enumerated(EnumType.STRING)
    private Set<CommonEnums.County> activityRegions = new HashSet<>();

    @Email(message = "Please provide a valid counselor email address")
    @Column(name = "counselor_email")
    private String counselorEmail;

    @Column(name = "dashboard_link")
    private String dashboardLink;

    @Email(message = "Please provide a valid roger email address")
    @Column(name = "roger_email")
    private String rogerEmail;

    @Email(message = "Please provide a valid permission email address")
    @Column(name = "permission_email")
    private String permissionEmail;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "is_m7_educator")
    private Boolean isM7Educator = false;

    @OneToMany(mappedBy = "school", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}