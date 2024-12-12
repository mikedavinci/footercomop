// Activity Record Controller Endpoints
// GET    /api/records                - Get all activity records (paginated)
// GET    /api/records/{id}           - Get activity record by ID
// POST   /api/records                - Create new activity record
// PUT    /api/records/{id}           - Update existing activity record
// GET    /api/records/user/{userId}  - Get user's activity records
// GET    /api/records/company/{companyId} - Get company's activity records


package wi.inspire.InspireWI.controller.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.inspire.InspireWI.DTO.Activity.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import wi.inspire.InspireWI.service.Activity.ActivityRecordService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
@Tag(name = "Activity Record", description = "Activity record management APIs")
public class ActivityRecordController {
    private final ActivityRecordService activityRecordService;

    // GET    /api/records - Get all activity records (paginated)
    @Operation(summary = "Get all activity records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved records"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ActivityRecordResponseDto>> getAllRecords(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(activityRecordService.findAll(pageable));
    }

    // GET    /api/records/{id} - Get activity record by ID
    @Operation(summary = "Get activity record by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved record"),
            @ApiResponse(responseCode = "404", description = "Record not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ActivityRecordResponseDto> getRecordById(
            @Parameter(description = "Record ID") @PathVariable UUID id) {
        return ResponseEntity.ok(activityRecordService.findById(id));
    }

    // POST   /api/records - Create new activity record
    @Operation(summary = "Create new activity record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created record"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Referenced entity not found")
    })
    @PostMapping
    public ResponseEntity<ActivityRecordResponseDto> createRecord(
            @Valid @RequestBody ActivityRecordDto recordDto) {
        return ResponseEntity.ok(activityRecordService.create(recordDto));
    }

    // PUT    /api/records/{id} - Update existing activity record
    @Operation(summary = "Update existing activity record")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated record"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Record not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ActivityRecordResponseDto> updateRecord(
            @PathVariable UUID id,
            @Valid @RequestBody ActivityRecordDto recordDto) {
        return ResponseEntity.ok(activityRecordService.update(id, recordDto));
    }

    // GET    /api/records/user/{userId} - Get user's activity records
    @Operation(summary = "Get user's activity records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved records"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ActivityRecordResponseDto>> getUserRecords(
            @PathVariable UUID userId,
            Pageable pageable) {
        return ResponseEntity.ok(activityRecordService.findByUserId(userId, pageable));
    }

    // GET    /api/records/company/{companyId} - Get company's activity records
    @Operation(summary = "Get company's activity records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved records"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @GetMapping("/company/{companyId}")
    public ResponseEntity<Page<ActivityRecordResponseDto>> getCompanyRecords(
            @PathVariable UUID companyId,
            Pageable pageable) {
        return ResponseEntity.ok(activityRecordService.findByCompanyId(companyId, pageable));
    }
}