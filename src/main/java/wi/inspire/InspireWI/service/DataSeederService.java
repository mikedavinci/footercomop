package wi.roger.rogerWI.service;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.CoordinatorCsvDto;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.repository.UserRepository;
import wi.roger.rogerWI.types.CommonEnums.County;
import wi.roger.rogerWI.types.CommonEnums.UserType;

import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataSeederService {
    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;  // Spring will inject this

    @Transactional
    public void seedData() {
        try {

            Resource resource = resourceLoader.getResource("classpath:data/rogerWICoordinators.csv");

            if (!resource.exists()) {
                throw new RuntimeException("CSV file not found in resources");
            }

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                List<CoordinatorCsvDto> coordinators = new CsvToBeanBuilder<CoordinatorCsvDto>(reader)
                        .withType(CoordinatorCsvDto.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build()
                        .parse();

                // Group by email to handle coordinators with multiple counties
                Map<String, List<CoordinatorCsvDto>> coordsByEmail = coordinators.stream()
                        .filter(c -> c.getEmail() != null && !c.getEmail().isEmpty())
                        .collect(Collectors.groupingBy(CoordinatorCsvDto::getEmail));

                for (Map.Entry<String, List<CoordinatorCsvDto>> entry : coordsByEmail.entrySet()) {
                    String email = entry.getKey();
                    List<CoordinatorCsvDto> coordEntries = entry.getValue();

                    // Skip if user already exists
                    if (userRepository.existsByEmail(email)) {
                        log.info("Coordinator with email {} already exists, skipping", email);
                        continue;
                    }

                    User coordinator = new User();
                    CoordinatorCsvDto firstEntry = coordEntries.get(0);

                    // Set basic info
                    coordinator.setEmail(email);
                    coordinator.setName(firstEntry.getName().replace(" (Regional Lead)", ""));
                    coordinator.setUserType(firstEntry.getName().contains("Regional Lead") ?
                            UserType.REGIONAL_LEADS : UserType.COORDINATOR);

                    // Collect all serving counties
                    Set<County> counties = coordEntries.stream()
                            .map(dto -> parseCounty(dto.getServingCounty()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    coordinator.setServingCounties(counties);

                    userRepository.save(coordinator);
                    log.info("Created coordinator: {}", coordinator.getName());
                }
            }

        } catch (Exception e) {
            log.error("Error during coordinator seeding: ", e);
            throw new RuntimeException("Error seeding coordinator data", e);
        }
    }

    private County parseCounty(String countyStr) {
        if (countyStr == null || countyStr.isEmpty()) return null;

        try {
            String enumFormat = countyStr.toUpperCase()
                    .replace(" ", "_")
                    .replace("-", "_");
            return County.valueOf(enumFormat);
        } catch (IllegalArgumentException e) {
            log.warn("Unknown county: {}", countyStr);
            return null;
        }
    }
}