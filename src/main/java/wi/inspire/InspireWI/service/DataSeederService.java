package wi.roger.rogerWI.service;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.CoordinatorCsvDto;
import wi.roger.rogerWI.model.School;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.repository.SchoolRepository;
import wi.roger.rogerWI.repository.UserRepository;
import wi.roger.rogerWI.types.CommonEnums;
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
    private final ResourceLoader resourceLoader;
    private final PasswordEncoder passwordEncoder;
    private final SchoolRepository schoolRepository;

    private static final String DEFAULT_PASSWORD = "rogerWI2024!";

    @Transactional
    public void seedData() {
        try {
            Resource resource = resourceLoader.getResource("classpath:data/rogerWICoordinators.csv");

            if (!resource.exists()) {
                throw new RuntimeException("CSV file not found in resources");
            }

            Map<String, School> schoolCache = new HashMap<>(); // Cache to avoid duplicate schools

            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream())) {
                List<CoordinatorCsvDto> coordinators = new CsvToBeanBuilder<CoordinatorCsvDto>(reader)
                        .withType(CoordinatorCsvDto.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build()
                        .parse();

                Map<String, List<CoordinatorCsvDto>> coordsByEmail = coordinators.stream()
                        .filter(c -> c.getEmail() != null && !c.getEmail().isEmpty())
                        .collect(Collectors.groupingBy(CoordinatorCsvDto::getEmail));

                for (Map.Entry<String, List<CoordinatorCsvDto>> entry : coordsByEmail.entrySet()) {
                    String email = entry.getKey();
                    List<CoordinatorCsvDto> coordEntries = entry.getValue();

                    User coordinator = userRepository.findByEmail(email)
                            .orElseGet(() -> {
                                // Create new user logic
                                User newCoord = new User();
                                CoordinatorCsvDto firstEntry = coordEntries.get(0);
                                newCoord.setEmail(email);
                                newCoord.setName(firstEntry.getName().replace(" (Regional Lead)", ""));
                                newCoord.setUserType(firstEntry.getName().contains("Regional Lead") ?
                                        UserType.REGIONAL_LEADS : UserType.COORDINATOR);
                                newCoord.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
                                return newCoord;
                            });

                    // Update counties for both new and existing users
                    Set<County> counties = coordEntries.stream()
                            .map(dto -> parseCounty(dto.getServingCounty()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
                    coordinator.setServingCounties(counties);

                    // Process schools for both new and existing users
                    Set<School> assignedSchools = new HashSet<>();
                    for (CoordinatorCsvDto coordEntry : coordEntries) {
                        if (coordEntry.getSchools() != null && !coordEntry.getSchools().isEmpty()) {
                            String[] schoolNames = coordEntry.getSchools().split(",");
                            for (String schoolName : schoolNames) {
                                String trimmedName = schoolName.trim();
                                School school = schoolCache.computeIfAbsent(trimmedName, name -> {
                                    return schoolRepository.findByName(name)
                                            .orElseGet(() -> {
                                                School newSchool = new School();
                                                newSchool.setName(name);
                                                // Set default access type to FULL_SERVICE
                                                newSchool.setAccessTo(CommonEnums.AccessTo.FULL_SERVICE);
                                                return schoolRepository.save(newSchool);
                                            });
                                });
                                assignedSchools.add(school);
                            }
                        }
                    }
                    coordinator.setAssignedSchools(assignedSchools);

                    coordinator = userRepository.save(coordinator);
                    log.info("{} coordinator: {} with email: {} and {} schools",
                            userRepository.existsByEmail(email) ? "Updated" : "Created",
                            coordinator.getName(),
                            coordinator.getEmail(),
                            assignedSchools.size());
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