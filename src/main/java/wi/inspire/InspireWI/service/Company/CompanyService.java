package wi.inspire.InspireWI.service.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.inspire.InspireWI.DTO.Company.CompanyRequestDto;
import wi.inspire.InspireWI.DTO.Company.CompanyResponseDto;
import wi.inspire.InspireWI.mapper.CompanyMapper;
import wi.inspire.InspireWI.service.ResourceExceptions.*;
import wi.inspire.InspireWI.model.*;
import wi.inspire.InspireWI.repository.*;
import wi.inspire.InspireWI.types.CommonEnums;

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
}