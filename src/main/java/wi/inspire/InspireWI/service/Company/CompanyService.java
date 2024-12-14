package wi.roger.rogerWI.service.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.Company.CompanyRequestDto;
import wi.roger.rogerWI.DTO.Company.CompanyResponseDto;
import wi.roger.rogerWI.mapper.CompanyMapper;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.service.ResourceExceptions.*;
import wi.roger.rogerWI.model.Company;
import wi.roger.rogerWI.repository.*;
import wi.roger.rogerWI.types.CommonEnums;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> findAll(Pageable pageable) {
        return companyRepository.findAll(pageable)
                .map(CompanyMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto findById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + id));
        return CompanyMapper.toResponse(company);
    }

    @Transactional
    public CompanyResponseDto create(CompanyRequestDto requestDto) {
        if (companyRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("Company name already exists");
        }

        Company company = CompanyMapper.toEntity(requestDto);
        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Transactional
    public CompanyResponseDto update(UUID id, CompanyRequestDto requestDto) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        if (!company.getName().equalsIgnoreCase(requestDto.getName()) &&
                companyRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("Company name already exists");
        }

        CompanyMapper.updateEntity(company, requestDto);
        return CompanyMapper.toResponse(companyRepository.save(company));
    }

    @Transactional(readOnly = true)
    public Page<CompanyResponseDto> findByCluster(CommonEnums.Cluster cluster, Pageable pageable) {
        return companyRepository.findByCareerCluster(cluster, pageable)
                .map(CompanyMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponseDto> findCompaniesByTourType() {
        return companyRepository.findByTourTypeIsNotNull()
                .stream()
                .map(CompanyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean isUserCompanyAdmin(UUID companyId, User user) {
        if (user == null || companyId == null) {
            return false;
        }

        // If user is ADMIN, they have access to all companies
        if (user.getUserType() == CommonEnums.UserType.ADMIN) {
            return true;
        }

        // For COMPANY_ADMIN, check if they're associated with this company
        if (user.getUserType() == CommonEnums.UserType.COMPANY_ADMIN) {
            return companyRepository.existsByIdAndCompanyAdminId(companyId, user.getId());
        }

        return false;
    }

    @Transactional(readOnly = true)
    public CompanyResponseDto getCompanyByAdmin(User user) {
        if (user.getUserType() != CommonEnums.UserType.COMPANY_ADMIN) {
            throw new AccessDeniedException("User is not a company admin");
        }

        Company company = companyRepository.findByCompanyAdminId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No company found for this admin"));

        return CompanyMapper.toResponse(company);
    }

}