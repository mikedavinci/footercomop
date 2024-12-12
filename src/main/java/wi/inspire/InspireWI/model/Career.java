package wi.inspire.InspireWI.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import wi.inspire.InspireWI.types.CommonEnums.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "careers")
public class Career {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @NotBlank(message = "Career name is required")
    @Column(nullable = false)
    private String name;

    @ElementCollection
    @CollectionTable(
            name = "career_clusters",
            joinColumns = @JoinColumn(name = "career_id")
    )
    @Column(name = "cluster")
    @Enumerated(EnumType.STRING)
    private Set<Cluster> clusters = new HashSet<>();

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}