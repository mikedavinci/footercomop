package wi.inspire.InspireWI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import wi.inspire.InspireWI.types.CommonEnums.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity(name = "career_pathways")
public class CareerPathway {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @NotBlank(message = "Career pathway name is required")
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "cluster", nullable = false)
    private Cluster cluster;

    @ManyToMany(mappedBy = "careerPathways", fetch = FetchType.LAZY)
    private Set<ActivityRecord> activityRecords = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "career_pathway_careers",
            joinColumns = @JoinColumn(name = "career_pathway_id"),
            inverseJoinColumns = @JoinColumn(name = "career_id")
    )
    private Set<Career> careers = new HashSet<>();

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}