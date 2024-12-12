// /api/v1/test/seed
package wi.inspire.InspireWI.controller.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wi.inspire.InspireWI.service.DataSeederService;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestDataSeederController {
    private final DataSeederService dataSeederService;

    @PostMapping("/seed")
    public ResponseEntity<String> testSeed() {
        try {
            log.info("Starting test seed");
            dataSeederService.seedData();
            return ResponseEntity.ok("Test seeding completed successfully");
        } catch (Exception e) {
            log.error("Error during test seeding: ", e);
            return ResponseEntity.internalServerError()
                    .body("Error during test seeding: " + e.getMessage());
        }
    }
}