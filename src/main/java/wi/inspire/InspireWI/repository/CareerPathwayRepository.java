package wi.roger.rogerWI.repository;

import wi.roger.rogerWI.model.CareerPathway;
import wi.roger.rogerWI.types.CommonEnums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface CareerPathwayRepository extends JpaRepository<CareerPathway, UUID> {
    List<CareerPathway> findByCluster(Cluster cluster);
    boolean existsByNameIgnoreCase(String name);
}