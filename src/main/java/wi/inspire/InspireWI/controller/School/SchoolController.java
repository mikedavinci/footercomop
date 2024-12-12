// School Controller Endpoints
// GET    /api/schools                - Get all schools (paginated)
// GET    /api/schools/{id}           - Get school by ID
// POST   /api/schools                - Create new school
// PUT    /api/schools/{id}           - Update existing school
// DELETE /api/schools/{id}           - Delete school
// GET    /api/schools/search         - Search schools by grade level/county/access type


package wi.roger.rogerWI.controller.School;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.roger.rogerWI.DTO.School.SchoolRequestDto;
import wi.roger.rogerWI.DTO.School.SchoolResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import wi.roger.rogerWI.service.School.SchoolService;
import wi.roger.rogerWI.types.CommonEnums.*;
import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
@Tag(name = "School", description = "School management APIs")
public class SchoolController {
    private final SchoolService schoolService;

    // GET    /api/schools - Get all schools (paginated)
    @Operation(summary = "Get all schools",
            description = "Returns a paginated list of all schools")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved schools"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<SchoolResponseDto>> getAllSchools(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(schoolService.findAll(pageable));
    }

    // GET    /api/schools/{id} - Get school by ID
    @Operation(summary = "Get school by ID",
            description = "Returns a single school by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved school"),
            @ApiResponse(responseCode = "404", description = "School not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponseDto> getSchoolById(
            @Parameter(description = "ID of the school to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(schoolService.findById(id));
    }

    // POST   /api/schools - Create new school
    @Operation(summary = "Create new school",
            description = "Creates a new school with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created school"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<SchoolResponseDto> createSchool(
            @Parameter(description = "School details") @Valid @RequestBody SchoolRequestDto requestDto) {
        return ResponseEntity.ok(schoolService.create(requestDto));
    }

    // PUT    /api/schools/{id} - Update existing school
    @Operation(summary = "Update existing school",
            description = "Updates an existing school with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated school"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "School not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponseDto> updateSchool(
            @Parameter(description = "ID of the school to update") @PathVariable UUID id,
            @Parameter(description = "Updated school details") @Valid @RequestBody SchoolRequestDto requestDto) {
        return ResponseEntity.ok(schoolService.update(id, requestDto));
    }

    // DELETE /api/schools/{id}- Delete school
    @Operation(summary = "Delete school",
            description = "Deletes an existing school by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted school"),
            @ApiResponse(responseCode = "404", description = "School not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(
            @Parameter(description = "ID of the school to delete") @PathVariable UUID id) {
        schoolService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET    /api/schools/search - Search schools by grade level/county/access type
    @Operation(summary = "Search schools",
            description = "Search schools by grade level, county, and access type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved schools"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<SchoolResponseDto>> searchSchools(
            @Parameter(description = "Grade level to filter by")
            @RequestParam(required = false) GradeLevel gradeLevel,
            @Parameter(description = "County to filter by")
            @RequestParam(required = false) County county,
            @Parameter(description = "Access type to filter by")
            @RequestParam(required = false) AccessTo accessTo,
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return ResponseEntity.ok(schoolService.searchSchools(gradeLevel, county, accessTo, pageable));
    }
}
