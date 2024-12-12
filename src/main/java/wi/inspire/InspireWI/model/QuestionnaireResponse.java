package wi.roger.rogerWI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class QuestionnaireResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type="org.hibernate.type.UUIDCharType")
    @Column(name = "id", updatable = false, nullable = false, length = 36, columnDefinition = "varchar(36)")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "activity_request_id")
    private ActivityRequest activityRequest;

    @OneToOne
    @JoinColumn(name = "activity_record_id")
    private ActivityRecord activityRecord;

    @ElementCollection
    @CollectionTable(
            name = "questionnaire_response_answers",
            joinColumns = @JoinColumn(name = "response_id")
    )
    @MapKeyColumn(name = "question_key")
    @Column(name = "answer")
    private Map<String, String> rawResponses = new HashMap<>();

    @CreatedDate
    @Column(name = "submitted_date", updatable = false)
    private LocalDateTime submittedDate;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;
}