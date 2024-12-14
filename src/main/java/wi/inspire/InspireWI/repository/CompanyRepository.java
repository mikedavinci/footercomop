package wi.roger.rogerWI.repository;

import wi.roger.rogerWI.model.Company;
import wi.roger.rogerWI.types.CommonEnums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {
    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM companies c " +
            "WHERE c.id = :companyId " +
            "AND c.companyAdmin.id = :userId")
    boolean existsByIdAndCompanyAdminId(@Param("companyId") UUID companyId, @Param("userId") UUID userId);


    Optional<Company> findByCompanyAdminId(UUID userId);

    @Query("SELECT c FROM companies c WHERE :cluster MEMBER OF c.clusters")
    Page<Company> findByCareerCluster(@Param("cluster") CommonEnums.Cluster cluster, Pageable pageable);

    @Query("SELECT c FROM companies c WHERE c.tourType IS NOT NULL")
    List<Company> findByTourTypeIsNotNull();

    @Query("SELECT c FROM companies c " +
            "WHERE (:name is null OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (COALESCE(:clusters, null) is null OR EXISTS (SELECT cl FROM c.clusters cl WHERE cl IN :clusters)) " +
            "AND (COALESCE(:regions, null) is null OR EXISTS (SELECT r FROM c.regions r WHERE r IN :regions)) " +
            "AND (:tourType is null OR c.tourType = :tourType)")
    Page<Company> findByFilters(
            @Param("name") String name,
            @Param("clusters") Set<CommonEnums.Cluster> clusters,
            @Param("regions") Set<CommonEnums.County> regions,
            @Param("tourType") CommonEnums.TourType tourType,
            Pageable pageable
    );

}