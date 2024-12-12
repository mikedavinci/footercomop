package wi.inspire.InspireWI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wi.inspire.InspireWI.model.ActivityRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ActivityRecordRepository extends JpaRepository<ActivityRecord, UUID> {
    Page<ActivityRecord> findByRecorderId(UUID recorderId, org.springframework.data.domain.Pageable pageable);
    @Query("SELECT ar FROM activity_records ar JOIN ar.participatingCompanies c WHERE c.id = :companyId")
    Page<ActivityRecord> findByParticipatingCompaniesId(@Param("companyId") UUID companyId, org.springframework.data.domain.Pageable pageable);
    @Query("SELECT ar FROM activity_records ar WHERE " +
            "(:activityId is null OR ar.activity.id = :activityId) AND " +
            "(:recorderId is null OR ar.recorder.id = :recorderId) AND " +
            "(:startDate is null OR ar.activityDate >= :startDate) AND " +
            "(:endDate is null OR ar.activityDate <= :endDate)")
    Page<ActivityRecord> findByFilters(
            @Param("activityId") UUID activityId,
            @Param("recorderId") UUID recorderId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            org.springframework.data.domain.Pageable pageable
    );

    List<ActivityRecord> findByActivityDateBetween(LocalDate startDate, LocalDate endDate);
}
