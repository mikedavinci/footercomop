// Company Controller Endpoints
// GET    /api/v1/companies                - Get all companies (paginated)
// GET    /api/v1/companies/{id}           - Get company by ID
// POST   /api/v1/companies                - Create new company
// PUT    /api/v1/companies/{id}           - Update company
// GET    /api/v1/companies/cluster/{id}   - Get companies by career cluster

package wi.roger.rogerWI.controller.Company;
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
import wi.roger.rogerWI.DTO.Company.CompanyRequestDto;
import wi.roger.rogerWI.DTO.Company.CompanyResponseDto;
import wi.roger.rogerWI.service.Company.CompanyService;
import wi.roger.rogerWI.types.CommonEnums.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Company", description = "Company management APIs")
public class CompanyController {
    private final CompanyService companyService;

    @Operation(summary = "Get all companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getAllCompanies(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(companyService.findAll(pageable));
    }

    @Operation(summary = "Get company by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved company"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(
            @Parameter(description = "Company ID") @PathVariable UUID id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @Operation(summary = "Create new company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created company"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
            @Valid @RequestBody CompanyRequestDto requestDto) {
        return ResponseEntity.ok(companyService.create(requestDto));
    }

    @Operation(summary = "Update company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated company"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequestDto requestDto) {
        return ResponseEntity.ok(companyService.update(id, requestDto));
    }

    @Operation(summary = "Get companies by career cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies"),
            @ApiResponse(responseCode = "400", description = "Invalid cluster")
    })
    @GetMapping("/cluster/{clusterId}")
    public ResponseEntity<Page<CompanyResponseDto>> getCompaniesByCluster(
            @PathVariable Cluster clusterId,
            Pageable pageable) {
        return ResponseEntity.ok(companyService.findByCluster(clusterId, pageable));
    }

    @Operation(summary = "Get companies offering tours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies")
    })
    @GetMapping("/tours")
    public ResponseEntity<List<CompanyResponseDto>> getCompaniesOfferingTours() {
        return ResponseEntity.ok(companyService.findCompaniesByTourType());
    }
}