package wi.roger.rogerWI.repository;

import wi.roger.rogerWI.model.Career;
import wi.roger.rogerWI.types.CommonEnums;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CareerRepository extends JpaRepository<Career, UUID> {
    @Query("SELECT c FROM careers c WHERE :cluster MEMBER OF c.clusters")
    Page<Career> findByCluster(@Param("cluster") CommonEnums.Cluster cluster, Pageable pageable);

    @Query("SELECT c FROM careers c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Career> findByNameContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT DISTINCT c FROM careers c JOIN c.clusters cluster WHERE cluster IN :clusters")
    List<Career> findByClusterIn(@Param("clusters") Set<CommonEnums.Cluster> clusters);

    boolean existsByNameIgnoreCase(String name);
}