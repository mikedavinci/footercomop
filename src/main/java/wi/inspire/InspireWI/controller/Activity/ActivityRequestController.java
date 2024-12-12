// Activity Request Controller Endpoints
// GET    /api/requests - Get all activity requests (paginated)
// GET    /api/requests/{id} - Get activity request by ID
// POST   /api/requests - Create new activity request
// PUT    /api/requests/{id} - Update existing activity request
// DELETE /api/requests/{id} - Delete activity request
// GET    /api/requests/user/{userId} - Get user's activity requests
// GET    /api/requests/pending - Get pending activity requests
// PATCH  /api/requests/{id}/status - Update activity request status


package wi.inspire.InspireWI.controller.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.inspire.InspireWI.DTO.Activity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import wi.inspire.InspireWI.service.Activity.ActivityRequestService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
@Tag(name = "Activity Request", description = "Activity request management APIs")
public class ActivityRequestController {
    private final ActivityRequestService activityRequestService;

    // GET    /api/requests - Get all activity requests (paginated)
    @Operation(summary = "Get all activity requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved requests"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ActivityRequestResponseDto>> getAllRequests(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(activityRequestService.findAll(pageable));
    }

    // GET    /api/requests/{id} - Get activity request by ID
    @Operation(summary = "Get activity request by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved request"),
            @ApiResponse(responseCode = "404", description = "Request not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ActivityRequestResponseDto> getRequestById(
            @Parameter(description = "Request ID") @PathVariable UUID id) {
        return ResponseEntity.ok(activityRequestService.findById(id));
    }

    // POST   /api/requests - Create new activity request
    @Operation(summary = "Create new activity request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created request"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Referenced entity not found")
    })
    @PostMapping
    public ResponseEntity<ActivityRequestResponseDto> createRequest(
            @Valid @RequestBody ActivityRequestDto requestDto) {
        return ResponseEntity.ok(activityRequestService.create(requestDto));
    }

    // PUT    /api/requests/{id} - Update existing activity request
    @Operation(summary = "Update activity request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated request"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActivityRequestResponseDto> updateRequest(
            @PathVariable UUID id,
            @Valid @RequestBody ActivityRequestDto requestDto) {
        return ResponseEntity.ok(activityRequestService.update(id, requestDto));
    }

    // DELETE /api/requests/{id} - Delete activity request
    @Operation(summary = "Delete activity request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted request"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable UUID id) {
        activityRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET    /api/requests/user/{userId} - Get user's activity requests
    @Operation(summary = "Get user's activity requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved requests"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ActivityRequestResponseDto>> getUserRequests(
            @PathVariable UUID userId,
            Pageable pageable) {
        return ResponseEntity.ok(activityRequestService.findByUserId(userId, pageable));
    }

    // GET    /api/requests/pending - Get pending activity requests
    @Operation(summary = "Get pending activity requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pending requests")
    })
    @GetMapping("/pending")
    public ResponseEntity<Page<ActivityRequestResponseDto>> getPendingRequests(Pageable pageable) {
        return ResponseEntity.ok(activityRequestService.findPendingRequests(pageable));
    }

    // PATCH  /api/requests/{id}/status - Update activity request status
    @Operation(summary = "Update activity request status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated request status"),
            @ApiResponse(responseCode = "404", description = "Request not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<ActivityRequestResponseDto> updateRequestStatus(
            @PathVariable UUID id,
            @RequestParam LocalDate completionDate) {
        return ResponseEntity.ok(activityRequestService.updateStatus(id, completionDate));
    }
}