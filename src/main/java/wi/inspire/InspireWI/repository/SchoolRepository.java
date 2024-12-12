package wi.inspire.InspireWI.repository;

import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import wi.inspire.InspireWI.model.School;
import wi.inspire.InspireWI.types.CommonEnums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchoolRepository extends JpaRepository<School, UUID> {
    @Query("SELECT s FROM schools s WHERE " +
            "(:gradeLevel is null OR s.gradeLevel = :gradeLevel) AND " +
            "(:county is null OR s.county = :county) AND " +
            "(:accessTo is null OR s.accessTo = :accessTo)")
    Page<School> findByFilters(
            @Param("gradeLevel") GradeLevel gradeLevel,
            @Param("county") County county,
            @Param("accessTo") AccessTo accessTo,
            Pageable pageable
    );

    @Query("SELECT s FROM schools s WHERE :county MEMBER OF s.activityRegions")
    List<School> findByActivityRegion(@Param("county") County county);
    List<School> findByIsM7Educator(Boolean isM7Educator);
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT DISTINCT s.county FROM schools s WHERE s.county IS NOT NULL")
    List<County> findAllCounties();
    Optional<School> findByInspireEmail(String inspireEmail);
    List<School> findByDistrictName(String districtName);

    @Query("SELECT DISTINCT s.districtName FROM schools s WHERE s.districtName IS NOT NULL")
    List<String> findAllDistinctDistrictNames();
    Page<School> findByGradeLevel(GradeLevel gradeLevel, Pageable pageable);

    @Query("SELECT s FROM schools s " +
            "LEFT JOIN FETCH s.users " +
            "WHERE s.id = :id")
    Optional<School> findByIdWithUsers(@Param("id") UUID id);

    @Query("SELECT s FROM schools s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.districtName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<School> searchByNameOrDistrict(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);

    @Query("SELECT s.gradeLevel, COUNT(s) FROM schools s GROUP BY s.gradeLevel")
    List<Object[]> countByGradeLevel();
}