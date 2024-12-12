package wi.roger.rogerWI.model;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "questionnaire_submissions")
@EntityListeners(AuditingEntityListener.class)
public class QuestionnaireSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_request_id", referencedColumnName = "id")
    private ActivityRequest activityRequest;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_record_id", referencedColumnName = "id")
    private ActivityRecord activityRecord;

    @Column(columnDefinition = "TEXT")
    private String rawData;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String imageData;

    @CreatedDate
    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;
}