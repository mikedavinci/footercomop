package wi.roger.rogerWI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wi.roger.rogerWI.model.QuestionnaireFlow;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionnaireFlowRepository extends JpaRepository<QuestionnaireFlow, UUID> {
    Optional<QuestionnaireFlow> findByUserIdAndCompletedFalse(UUID userId);
}