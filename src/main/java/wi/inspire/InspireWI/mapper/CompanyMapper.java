package wi.roger.rogerWI.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wi.roger.rogerWI.DTO.Company.CompanyRequestDto;
import wi.roger.rogerWI.DTO.Company.CompanyResponseDto;
import wi.roger.rogerWI.DTO.Company.CompanyReferenceDto;
import wi.roger.rogerWI.DTO.Company.CompanyListDto;
import wi.roger.rogerWI.model.Company;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class CompanyMapper {

    public static CompanyResponseDto toResponse(Company company) {
        if (company == null) return null;

        return CompanyResponseDto.builder()
                .id(company.getId())
                .name(company.getName())
                .regions(company.getRegions())
                .clusters(company.getClusters())
                .activities(company.getActivities())
                .contactName(company.getContactName())
                .contactPhone(company.getContactPhone())
                .contactEmail(company.getContactEmail())
                .dashboardLink(company.getDashboardLink())
                .areasOfInterest(company.getAreasOfInterest())
                .tourType(company.getTourType())
                .createdDate(company.getCreatedDate())
                .updatedDate(company.getUpdatedDate())
                .build();
    }

    public static Company toEntity(CompanyRequestDto request) {
        if (request == null) return null;

        Company company = new Company();
        updateEntity(company, request);
        return company;
    }

    public static void updateEntity(Company company, CompanyRequestDto request) {
        if (company == null || request == null) return;

        company.setName(request.getName());
        company.setRegions(request.getRegions() != null ? request.getRegions() : new HashSet<>());
        company.setClusters(request.getClusters() != null ? request.getClusters() : new HashSet<>());
        company.setActivities(request.getActivities() != null ? request.getActivities() : new HashSet<>());
        company.setContactName(request.getContactName());
        company.setContactPhone(request.getContactPhone());
        company.setContactEmail(request.getContactEmail());
        company.setDashboardLink(request.getDashboardLink());
        company.setAreasOfInterest(request.getAreasOfInterest());
        company.setTourType(request.getTourType());
    }

    public static CompanyReferenceDto toReference(Company company) {
        if (company == null) return null;

        return CompanyReferenceDto.builder()
                .id(company.getId())
                .name(company.getName())
                .build();
    }

    public static CompanyListDto toListResponse(Company company) {
        if (company == null) return null;

        return CompanyListDto.builder()
                .id(company.getId())
                .name(company.getName())
                .regions(company.getRegions())
                .clusters(company.getClusters())
                .activities(company.getActivities())
                .contactName(company.getContactName())
                .build();
    }
}