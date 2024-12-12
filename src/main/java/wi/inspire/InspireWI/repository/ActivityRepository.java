package wi.inspire.InspireWI.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wi.inspire.InspireWI.model.Activity;
import wi.inspire.InspireWI.model.CareerPathway;
import wi.inspire.InspireWI.types.CommonEnums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    @Query("SELECT a FROM activities a WHERE " +
            "(:accessLevel is null OR a.accessLevel = :accessLevel) AND " +
            "(:accessType is null OR :accessType MEMBER OF a.accessTypes) AND " +
            "(:category is null OR a.category = :category)")
    Page<Activity> findByFilters(
            @Param("accessLevel") AccessLevel accessLevel,
            @Param("accessType") AccessType accessType,
            @Param("category") ActivityCategory category,
            Pageable pageable
    );

    List<Activity> findByAccessTypesContaining(AccessType accessType);
    List<Activity> findByAccessLevel(AccessLevel accessLevel);
    List<Activity> findByAvailablePathwaysContaining(CareerPathway pathway);
    List<Activity> findByCategory(ActivityCategory category);
    @Query("SELECT DISTINCT a FROM activities a JOIN a.availablePathways p WHERE p.id = :pathwayId")
    List<Activity> findByPathwayId(@Param("pathwayId") UUID pathwayId);
    boolean existsByNameIgnoreCase(String name);
    Page<Activity> findByIsPrescheduled(Boolean isPrescheduled, Pageable pageable);
    Page<Activity> findByDeliveryMode(DeliveryMode deliveryMode, Pageable pageable);

    @Query("SELECT a FROM activities a WHERE " +
            "(:query is null OR LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
            "AND (COALESCE(:categories, null) is null OR a.category IN :categories) " +
            "AND (:deliveryMode is null OR a.deliveryMode = :deliveryMode) " +
            "AND (:isPrescheduled is null OR a.isPrescheduled = :isPrescheduled)")
    Page<Activity> findByFilters(
            @Param("query") String query,
            @Param("categories") Set<ActivityCategory> categories,
            @Param("deliveryMode") DeliveryMode deliveryMode,
            @Param("isPrescheduled") Boolean isPrescheduled,
            Pageable pageable);

}