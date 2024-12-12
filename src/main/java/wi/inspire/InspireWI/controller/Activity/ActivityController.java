// Activity Controller Endpoints
// GET    /api/activities                - Get all activities (paginated)
// GET    /api/activities/{id}           - Get activity by ID
// POST   /api/activities                - Create new activity
// PUT    /api/activities/{id}           - Update existing activity
// DELETE /api/activities/{id}           - Delete activity
// GET    /api/activities/search         - Search activities by access level/type/category

package wi.inspire.InspireWI.controller.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.inspire.InspireWI.DTO.Activity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import wi.inspire.InspireWI.service.Activity.ActivityService;
import wi.inspire.InspireWI.types.CommonEnums.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/activities")
@RequiredArgsConstructor
@Tag(name = "Activity", description = "Activity management APIs")
public class ActivityController {
    private final ActivityService activityService;

    // GET    /api/activities - Get all activities (paginated)
    @Operation(summary = "Get all activities",
            description = "Returns a paginated list of all activities")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved activities"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ActivityResponseDto>> getAllActivities(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(activityService.findAll(pageable));
    }

    // GET    /api/activities/{id} - Get activity by ID
    @Operation(summary = "Get activity by ID",
            description = "Returns a single activity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved activity"),
            @ApiResponse(responseCode = "404", description = "Activity not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> getActivityById(
            @Parameter(description = "ID of the activity to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(activityService.findById(id));
    }

    // POST   /api/activities - Create new activity
    @Operation(summary = "Create new activity",
            description = "Creates a new activity with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created activity"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<ActivityResponseDto> createActivity(
            @Parameter(description = "Activity details") @Valid @RequestBody ActivityCreateDto requestDto) {
        return ResponseEntity.ok(activityService.create(requestDto));
    }

    // PUT    /api/activities/{id} - Update existing activity
    @Operation(summary = "Update existing activity",
            description = "Updates an existing activity with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated activity"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Activity not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponseDto> updateActivity(
            @Parameter(description = "ID of the activity to update") @PathVariable UUID id,
            @Parameter(description = "Updated activity details") @Valid @RequestBody ActivityCreateDto requestDto) {
        return ResponseEntity.ok(activityService.update(id, requestDto));
    }

    // DELETE /api/activities/{id} - Delete activity
    @Operation(summary = "Delete activity",
            description = "Deletes an existing activity by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted activity"),
            @ApiResponse(responseCode = "404", description = "Activity not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(
            @Parameter(description = "ID of the activity to delete") @PathVariable UUID id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET    /api/activities/search - Search activities by access level/type/category
    @Operation(summary = "Search activities",
            description = "Search activities by access level, type, and category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved activities"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ActivityResponseDto>> searchActivities(
            @Parameter(description = "Access level to filter by")
            @RequestParam(required = false) AccessLevel accessLevel,
            @Parameter(description = "Access type to filter by")
            @RequestParam(required = false) AccessType accessType,
            @Parameter(description = "Activity category to filter by")
            @RequestParam(required = false) ActivityCategory category,
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return ResponseEntity.ok(activityService.searchActivities(accessLevel, accessType, category, pageable));
    }

    // GET    /api/activities/prescheduled - Get prescheduled activities
    @Operation(summary = "Get prescheduled activities")
    @GetMapping("/prescheduled")
    public ResponseEntity<Page<ActivityResponseDto>> getPrescheduledActivities(Pageable pageable) {
        return ResponseEntity.ok(activityService.findPrescheduledActivities(pageable));
    }

    // GET    /api/activities/virtual - Get virtual activities
    @Operation(summary = "Get virtual activities")
    @GetMapping("/virtual")
    public ResponseEntity<Page<ActivityResponseDto>> getVirtualActivities(Pageable pageable) {
        return ResponseEntity.ok(activityService.findByDeliveryMode(DeliveryMode.VIRTUAL, pageable));
    }

    // GET    /api/activities/in-person - Get in-person activities
    @Operation(summary = "Get in-person activities")
    @GetMapping("/in-person")
    public ResponseEntity<Page<ActivityResponseDto>> getInPersonActivities(Pageable pageable) {
        return ResponseEntity.ok(activityService.findByDeliveryMode(DeliveryMode.IN_PERSON, pageable));
    }
}