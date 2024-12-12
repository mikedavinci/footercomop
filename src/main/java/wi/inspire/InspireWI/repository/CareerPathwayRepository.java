package wi.inspire.InspireWI.repository;

import wi.inspire.InspireWI.model.CareerPathway;
import wi.inspire.InspireWI.types.CommonEnums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CareerPathwayRepository extends JpaRepository<CareerPathway, UUID> {
    List<CareerPathway> findByCluster(Cluster cluster);
    boolean existsByNameIgnoreCase(String name);
}