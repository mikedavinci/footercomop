package wi.roger.rogerWI.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wi.roger.rogerWI.model.ActivityRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wi.roger.rogerWI.types.CommonEnums;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRequestRepository extends JpaRepository<ActivityRequest, UUID> {
    Page<ActivityRequest> findByRequestorId(UUID userId, Pageable pageable);

    Page<ActivityRequest> findByCompletionDateIsNull(Pageable pageable);

    @Query("SELECT DISTINCT ar FROM activity_requests ar " +
            "LEFT JOIN ar.companies c " +
            "WHERE (:userId is null OR ar.requestor.id = :userId) " +
            "AND (:activityId is null OR ar.activity.id = :activityId) " +
            "AND (:companyId is null OR c.id = :companyId) " +
            "AND (:completed is null OR " +
            "(:completed = true AND ar.completionDate IS NOT NULL) OR " +
            "(:completed = false AND ar.completionDate IS NULL))")
    Page<ActivityRequest> findByFilters(
            @Param("userId") UUID userId,
            @Param("activityId") UUID activityId,
            @Param("companyId") UUID companyId,
            @Param("completed") Boolean completed,
            Pageable pageable
    );

    @Query("SELECT DISTINCT ar FROM activity_requests ar " +
            "JOIN ar.participantGrades pg " +
            "WHERE pg = :gradeLevel")
    List<ActivityRequest> findByGradeLevel(@Param("gradeLevel") CommonEnums.GradeLevel gradeLevel);

    List<ActivityRequest> findByPreferredDateBetween(LocalDate startDate, LocalDate endDate);

    List<ActivityRequest> findByRequestorIdAndActivityId(UUID userId, UUID activityId);

    boolean existsByRequestorIdAndActivityIdAndCompletionDateIsNull(UUID userId, UUID activityId);
}