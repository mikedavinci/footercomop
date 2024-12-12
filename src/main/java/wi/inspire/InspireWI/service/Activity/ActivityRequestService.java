package wi.roger.rogerWI.service.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.Activity.*;
import wi.roger.rogerWI.mapper.ActivityMapper;
import wi.roger.rogerWI.model.Activity;
import wi.roger.rogerWI.model.ActivityRequest;
import wi.roger.rogerWI.model.Company;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.repository.ActivityRepository;
import wi.roger.rogerWI.repository.ActivityRequestRepository;
import wi.roger.rogerWI.repository.CompanyRepository;
import wi.roger.rogerWI.repository.UserRepository;
import wi.roger.rogerWI.service.ResourceExceptions.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class ActivityRequestService {
    private final ActivityRequestRepository activityRequestRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public Page<ActivityRequestResponseDto> findAll(Pageable pageable) {
        return activityRequestRepository.findAll(pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ActivityRequestResponseDto findById(UUID id) {
        ActivityRequest request = activityRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity request not found with id: " + id));
        return ActivityMapper.toResponse(request);
    }

    @Transactional
    public ActivityRequestResponseDto create(ActivityRequestDto requestDto) {
        User requestor = userRepository.findById(requestDto.getRequestorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Activity activity = activityRepository.findById(requestDto.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        ActivityRequest request = ActivityMapper.toEntity(requestDto);
        request.setRequestor(requestor);
        request.setActivity(activity);

        if (requestDto.getCompanyIds() != null && !requestDto.getCompanyIds().isEmpty()) {
            Set<Company> companies = requestDto.getCompanyIds().stream()
                    .map(companyId -> companyRepository.findById(companyId)
                            .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId)))
                    .collect(Collectors.toSet());
            request.setCompanies(companies);
        }

        return ActivityMapper.toResponse(activityRequestRepository.save(request));
    }

    @Transactional
    public ActivityRequestResponseDto update(UUID id, ActivityRequestDto requestDto) {
        ActivityRequest request = activityRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity request not found"));

        ActivityMapper.updateEntity(request, requestDto);

        if (requestDto.getCompanyIds() != null && !requestDto.getCompanyIds().isEmpty()) {
            Set<Company> companies = requestDto.getCompanyIds().stream()
                    .map(companyId -> companyRepository.findById(companyId)
                            .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId)))
                    .collect(Collectors.toSet());
            request.setCompanies(companies);
        }

        return ActivityMapper.toResponse(activityRequestRepository.save(request));
    }

    @Transactional
    public void delete(UUID id) {
        if (!activityRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity request not found with id: " + id);
        }
        activityRequestRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ActivityRequestResponseDto> findByUserId(UUID userId, Pageable pageable) {
        return activityRequestRepository.findByRequestorId(userId, pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ActivityRequestResponseDto> findPendingRequests(Pageable pageable) {
        return activityRequestRepository.findByCompletionDateIsNull(pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional
    public ActivityRequestResponseDto updateStatus(UUID id, LocalDate completionDate) {
        ActivityRequest request = activityRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity request not found"));
        request.setCompletionDate(completionDate);
        return ActivityMapper.toResponse(activityRequestRepository.save(request));
    }
}