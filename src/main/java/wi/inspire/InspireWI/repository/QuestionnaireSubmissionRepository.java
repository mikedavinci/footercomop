package wi.roger.rogerWI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wi.roger.rogerWI.model.QuestionnaireSubmission;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionnaireSubmissionRepository extends JpaRepository<QuestionnaireSubmission, UUID> {
    List<QuestionnaireSubmission> findByUserId(UUID userId);

    @Query("SELECT qs FROM questionnaire_submissions qs JOIN qs.user u WHERE u.email = :email ORDER BY qs.submittedDate DESC")
    List<QuestionnaireSubmission> findByUserEmail(@Param("email") String email);
}