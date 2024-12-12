// /model/User.java
package wi.roger.rogerWI.model;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.*;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import wi.roger.rogerWI.types.CommonEnums.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "userId", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(name = "user_type", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(name = "deployment", length = 50)
    private String deployment;

    @Column(name = "title", length = 100)
    private String title;

    @ElementCollection
    @CollectionTable(
            name = "user_serving_counties",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "county")
    @Enumerated(EnumType.STRING)
    private Set<County> servingCounties = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_assigned_schools",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "school_id")
    )
    private Set<School> assignedSchools = new HashSet<>();

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "work_phone", length = 20)
    private String workPhone;

    @Column(name = "phone_type", length = 10)
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @Column(name = "sms_consent", columnDefinition = "boolean default false")
    private Boolean smsConsent = false;

    @Column(name = "parent_guardian_email", length = 100)
    private String parentGuardianEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", referencedColumnName = "id")
    private School school;

    @Column(name = "student_id", length = 50)
    private String studentId;

    @Column(name = "intended_major", length = 100)
    private String intendedMajor;

    @Column(name = "grade_level", length = 20)
    @Enumerated(EnumType.STRING)
    private GradeLevel gradeLevel;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "educator_role", length = 30)
    @Enumerated(EnumType.STRING)
    private EducatorRole educatorRole;

    @Column(name = "custom_role", length = 100)
    private String customRole;

    @CreatedDate
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

}