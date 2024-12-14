// Company Controller Endpoints
// GET    /api/v1/companies                - Get all companies (paginated)
// GET    /api/v1/companies/{id}           - Get company by ID
// POST   /api/v1/companies                - Create new company
// PUT    /api/v1/companies/{id}           - Update company
// GET    /api/v1/companies/cluster/{id}   - Get companies by career cluster

package wi.roger.rogerWI.controller.Company;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import wi.roger.rogerWI.DTO.Company.CompanyRequestDto;
import wi.roger.rogerWI.DTO.Company.CompanyResponseDto;
import wi.roger.rogerWI.SecurityConfiguration.CurrentUser;
import wi.roger.rogerWI.model.User;
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

    // GET    /api/v1/companies - Get all companies (paginated)
    // Anyone can view companies
    @Operation(summary = "Get all companies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDto>> getAllCompanies(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(companyService.findAll(pageable));
    }

    // GET    /api/v1/companies/{id} - Get company by ID
    // Anyone can view a specific company
    @Operation(summary = "Get company by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved company"),
            @ApiResponse(responseCode = "404", description = "Company not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("permitAll()")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(
            @Parameter(description = "Company ID") @PathVariable UUID id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    // POST   /api/v1/companies - Create new company
    // Only ADMIN and COMPANY_ADMIN can create companies
    @Operation(summary = "Create new company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created company"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'COMPANY_ADMIN')")
    @PostMapping
    public ResponseEntity<CompanyResponseDto> createCompany(
            @Valid @RequestBody CompanyRequestDto requestDto) {
        return ResponseEntity.ok(companyService.create(requestDto));
    }

    // PUT    /api/v1/companies/{id} - Update company
    // Only ADMIN and COMPANY_ADMIN can update companies
    @Operation(summary = "Update company")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated company"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Company not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN') or " +
            "(hasRole('COMPANY_ADMIN') and @companyService.isUserCompanyAdmin(#id, authentication.principal))")
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(
            @PathVariable UUID id,
            @Valid @RequestBody CompanyRequestDto requestDto) {
        return ResponseEntity.ok(companyService.update(id, requestDto));
    }

    // GET    /api/v1/companies/cluster/{id} - Get companies by career cluster
    // Anyone can view companies by cluster
    @Operation(summary = "Get companies by career cluster")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies"),
            @ApiResponse(responseCode = "400", description = "Invalid cluster")
    })
    @PreAuthorize("permitAll()")
    @GetMapping("/cluster/{clusterId}")
    public ResponseEntity<Page<CompanyResponseDto>> getCompaniesByCluster(
            @PathVariable Cluster clusterId,
            Pageable pageable) {
        return ResponseEntity.ok(companyService.findByCluster(clusterId, pageable));
    }

    // GET    /api/v1/companies/tours - Get companies offering tours
    // Anyone can view companies offering tours
    @Operation(summary = "Get companies offering tours")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved companies")
    })
    @PreAuthorize("permitAll()")
    @GetMapping("/tours")
    public ResponseEntity<List<CompanyResponseDto>> getCompaniesOfferingTours() {
        return ResponseEntity.ok(companyService.findCompaniesByTourType());
    }

    // GET    /api/v1/companies/my-company - Get my company (for COMPANY_ADMIN)
    // Get my company (for COMPANY_ADMIN)
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('COMPANY_ADMIN')")
    @GetMapping("/my-company")
    public ResponseEntity<CompanyResponseDto> getMyCompany(@CurrentUser User user) {
        return ResponseEntity.ok(companyService.getCompanyByAdmin(user));
    }
}