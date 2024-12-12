package wi.roger.rogerWI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wi.roger.rogerWI.model.QuestionnaireResponse;

import java.util.UUID;

@Repository
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, UUID> {
    Page<QuestionnaireResponse> findByUserId(UUID userId, Pageable pageable);
}