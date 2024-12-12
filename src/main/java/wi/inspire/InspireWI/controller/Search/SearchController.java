// Search Controller Endpoints
// GET    /api/v1/search/activities - Search activities
// GET    /api/v1/search/companies  - Search companies
// GET    /api/v1/search/users      - Search users

package wi.roger.rogerWI.controller.Search;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wi.roger.rogerWI.DTO.Activity.ActivityListDto;
import wi.roger.rogerWI.DTO.Company.CompanyListDto;
import wi.roger.rogerWI.DTO.User.UserListResponseDto;
import wi.roger.rogerWI.service.Search.SearchService;
import wi.roger.rogerWI.types.CommonEnums.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Search APIs")
public class SearchController {
    private final SearchService searchService;

    @Operation(summary = "Search activities")
    @GetMapping("/activities")
    public ResponseEntity<Page<ActivityListDto>> searchActivities(
            @Parameter(description = "Activity name") @RequestParam(required = false) String query,
            @Parameter(description = "Activity categories") @RequestParam(required = false) Set<ActivityCategory> categories,
            @Parameter(description = "Delivery mode") @RequestParam(required = false) DeliveryMode deliveryMode,
            @Parameter(description = "Is prescheduled") @RequestParam(required = false) Boolean isPrescheduled,
            Pageable pageable) {
        return ResponseEntity.ok(searchService.searchActivities(query, categories, deliveryMode, isPrescheduled, pageable));
    }

    @Operation(summary = "Search companies")
    @GetMapping("/companies")
    public ResponseEntity<Page<CompanyListDto>> searchCompanies(
            @Parameter(description = "Company name") @RequestParam(required = false) String name,
            @Parameter(description = "Career clusters") @RequestParam(required = false) Set<Cluster> clusters,
            @Parameter(description = "Regions") @RequestParam(required = false) Set<County> regions,
            @Parameter(description = "Tour type") @RequestParam(required = false) TourType tourType,
            Pageable pageable) {
        return ResponseEntity.ok(searchService.searchCompanies(name, clusters, regions, tourType, pageable));
    }

    @Operation(summary = "Search users")
    @GetMapping("/users")
    public ResponseEntity<Page<UserListResponseDto>> searchUsers(
            @Parameter(description = "Email") @RequestParam(required = false) String email,
            @Parameter(description = "Name") @RequestParam(required = false) String name,
            @Parameter(description = "User types") @RequestParam(required = false) Set<UserType> userTypes,
            Pageable pageable) {
        return ResponseEntity.ok(searchService.searchUsers(email, name, userTypes, pageable));
    }
}