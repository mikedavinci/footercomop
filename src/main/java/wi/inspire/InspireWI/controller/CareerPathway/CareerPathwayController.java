// CareerPathways Controller Endpoints
// GET    /api/career-pathways - Get all career pathways (paginated)
// GET    /api/career-pathways/{id} - Get career pathway by ID
// POST   /api/career-pathways - Create new career pathway
// PUT    /api/career-pathways/{id} - Update existing career pathway
// DELETE /api/career-pathways/{id} - Delete career pathway
// GET    /api/career-pathways/cluster/{cluster} - Get pathways by cluster

package wi.roger.rogerWI.controller.CareerPathway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import wi.roger.rogerWI.DTO.Career.CareerPathwayRequestDto;
import wi.roger.rogerWI.DTO.Career.CareerPathwayResponseDto;
import wi.roger.rogerWI.service.CareerPathway.CareerPathwayService;
import wi.roger.rogerWI.types.CommonEnums.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/career-pathways")
@RequiredArgsConstructor
@Tag(name = "Career Pathway", description = "Career pathway management APIs")
public class CareerPathwayController {
    private final CareerPathwayService careerPathwayService;

    // GET    /api/career-pathways - Get all career pathways (paginated)
    @Operation(summary = "Get all career pathways")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pathways"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CareerPathwayResponseDto>> getAllPathways(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(careerPathwayService.findAll(pageable));
    }

    // GET    /api/career-pathways/{id} - Get career pathway by ID
    @Operation(summary = "Get career pathway by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pathway"),
            @ApiResponse(responseCode = "404", description = "Pathway not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CareerPathwayResponseDto> getPathwayById(
            @Parameter(description = "Pathway ID") @PathVariable UUID id) {
        return ResponseEntity.ok(careerPathwayService.findById(id));
    }

    // POST   /api/career-pathways - Create new career pathway
    @Operation(summary = "Create new career pathway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created pathway"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<CareerPathwayResponseDto> createPathway(
            @Valid @RequestBody CareerPathwayRequestDto requestDto) {
        return ResponseEntity.ok(careerPathwayService.create(requestDto));
    }

    // PUT    /api/career-pathways/{id} - Update existing career pathway
    @Operation(summary = "Update career pathway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated pathway"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Pathway not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CareerPathwayResponseDto> updatePathway(
            @PathVariable UUID id,
            @Valid @RequestBody CareerPathwayRequestDto requestDto) {
        return ResponseEntity.ok(careerPathwayService.update(id, requestDto));
    }

    // DELETE /api/career-pathways/{id} - Delete career pathway
    @Operation(summary = "Delete career pathway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted pathway"),
            @ApiResponse(responseCode = "404", description = "Pathway not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePathway(@PathVariable UUID id) {
        careerPathwayService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET    /api/career-pathways/cluster/{cluster} - Get pathways by cluster
    @Operation(summary = "Get pathways by cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pathways"),
            @ApiResponse(responseCode = "400", description = "Invalid cluster")
    })
    @GetMapping("/cluster/{cluster}")
    public ResponseEntity<List<CareerPathwayResponseDto>> getPathwaysByCluster(
            @PathVariable Cluster cluster) {
        return ResponseEntity.ok(careerPathwayService.findByCluster(cluster));
    }
}