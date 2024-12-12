package wi.inspire.InspireWI.service.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.inspire.InspireWI.DTO.Activity.*;
import wi.inspire.InspireWI.mapper.ActivityMapper;
import wi.inspire.InspireWI.model.Activity;
import wi.inspire.InspireWI.model.Company;
import wi.inspire.InspireWI.model.User;
import wi.inspire.InspireWI.repository.ActivityRepository;
import wi.inspire.InspireWI.repository.CompanyRepository;
import wi.inspire.InspireWI.repository.UserRepository;
import wi.inspire.InspireWI.service.ResourceExceptions.*;

import wi.inspire.InspireWI.model.*;
import wi.inspire.InspireWI.repository.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityRecordService {
    private final ActivityRecordRepository activityRecordRepository;
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final CompanyRepository companyRepository;
    private final CareerPathwayRepository careerPathwayRepository;

    @Transactional(readOnly = true)
    public Page<ActivityRecordResponseDto> findAll(Pageable pageable) {
        return activityRecordRepository.findAll(pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ActivityRecordResponseDto findById(UUID id) {
        ActivityRecord record = activityRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity record not found with id: " + id));
        return ActivityMapper.toResponse(record);
    }

    @Transactional
    public ActivityRecordResponseDto create(ActivityRecordDto recordDto) {
        User recorder = userRepository.findById(recordDto.getRecorderId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Activity activity = activityRepository.findById(recordDto.getActivityId())
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));

        ActivityRecord record = ActivityMapper.toEntity(recordDto);
        record.setRecorder(recorder);
        record.setActivity(activity);

        // Handle company assignments
        if (recordDto.getParticipatingCompanyIds() != null && !recordDto.getParticipatingCompanyIds().isEmpty()) {
            Set<Company> companies = recordDto.getParticipatingCompanyIds().stream()
                    .map(companyId -> companyRepository.findById(companyId)
                            .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId)))
                    .collect(Collectors.toSet());
            record.setParticipatingCompanies(companies);
        }

        // Handle career pathway assignments
        if (recordDto.getCareerPathwayIds() != null && !recordDto.getCareerPathwayIds().isEmpty()) {
            Set<CareerPathway> pathways = recordDto.getCareerPathwayIds().stream()
                    .map(pathwayId -> careerPathwayRepository.findById(pathwayId)
                            .orElseThrow(() -> new ResourceNotFoundException("Career pathway not found with id: " + pathwayId)))
                    .collect(Collectors.toSet());
            record.setCareerPathways(pathways);
        }

        return ActivityMapper.toResponse(activityRecordRepository.save(record));
    }

    @Transactional
    public ActivityRecordResponseDto update(UUID id, ActivityRecordDto recordDto) {
        ActivityRecord record = activityRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity record not found"));

        ActivityMapper.updateEntity(record, recordDto);

        // Handle company assignments
        if (recordDto.getParticipatingCompanyIds() != null && !recordDto.getParticipatingCompanyIds().isEmpty()) {
            Set<Company> companies = recordDto.getParticipatingCompanyIds().stream()
                    .map(companyId -> companyRepository.findById(companyId)
                            .orElseThrow(() -> new ResourceNotFoundException("Company not found with id: " + companyId)))
                    .collect(Collectors.toSet());
            record.setParticipatingCompanies(companies);
        }

        // Handle career pathway assignments
        if (recordDto.getCareerPathwayIds() != null && !recordDto.getCareerPathwayIds().isEmpty()) {
            Set<CareerPathway> pathways = recordDto.getCareerPathwayIds().stream()
                    .map(pathwayId -> careerPathwayRepository.findById(pathwayId)
                            .orElseThrow(() -> new ResourceNotFoundException("Career pathway not found with id: " + pathwayId)))
                    .collect(Collectors.toSet());
            record.setCareerPathways(pathways);
        }

        return ActivityMapper.toResponse(activityRecordRepository.save(record));
    }

    @Transactional(readOnly = true)
    public Page<ActivityRecordResponseDto> findByUserId(UUID userId, Pageable pageable) {
        return activityRecordRepository.findByRecorderId(userId, pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ActivityRecordResponseDto> findByCompanyId(UUID companyId, Pageable pageable) {
        return activityRecordRepository.findByParticipatingCompaniesId(companyId, pageable)
                .map(ActivityMapper::toResponse);
    }
}
