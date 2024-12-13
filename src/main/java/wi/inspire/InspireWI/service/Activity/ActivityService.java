package wi.roger.rogerWI.service.Activity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.Activity.ActivityCreateDto;
import wi.roger.rogerWI.DTO.Activity.ActivityResponseDto;
import wi.roger.rogerWI.mapper.ActivityMapper;
import wi.roger.rogerWI.model.Activity;
import wi.roger.rogerWI.model.ActivityRequest;
import wi.roger.rogerWI.model.CareerPathway;
import wi.roger.rogerWI.repository.ActivityRepository;
import wi.roger.rogerWI.repository.ActivityRequestRepository;
import wi.roger.rogerWI.repository.CareerPathwayRepository;
import wi.roger.rogerWI.service.ResourceExceptions.*;
import wi.roger.rogerWI.types.CommonEnums.*;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final CareerPathwayRepository careerPathwayRepository;
    private final ActivityRequestRepository activityRequestRepository;



    public ActivityRequest saveActivityRequest(ActivityRequest request) {
        return activityRequestRepository.save(request);
    }

    public Activity getActivityEntity(UUID id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> findAll(Pageable pageable) {
        return activityRepository.findAll(pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ActivityResponseDto findById(UUID id) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));
        return ActivityMapper.toResponse(activity);
    }

//    public Activity findById(UUID id) {
//        return activityRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));
//    }

    @Transactional
    public ActivityResponseDto create(ActivityCreateDto requestDto) {
        if (activityRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("Activity name already exists");
        }

        Activity activity = ActivityMapper.toEntity(requestDto);

        // Handle pathway assignments if provided
        return getActivityResponseDto(requestDto, activity);
    }

    private ActivityResponseDto getActivityResponseDto(ActivityCreateDto requestDto, Activity activity) {
        if (requestDto.getPathwayIds() != null && !requestDto.getPathwayIds().isEmpty()) {
            Set<CareerPathway> pathways = requestDto.getPathwayIds().stream()
                    .map(pathwayId -> careerPathwayRepository.findById(pathwayId)
                            .orElseThrow(() -> new ResourceNotFoundException("Career Pathway not found with id: " + pathwayId)))
                    .collect(Collectors.toSet());
            activity.setAvailablePathways(pathways);
        }

        return ActivityMapper.toResponse(activityRepository.save(activity));
    }

    @Transactional
    public ActivityResponseDto update(UUID id, ActivityCreateDto requestDto) {
        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found with id: " + id));

        if (!activity.getName().equalsIgnoreCase(requestDto.getName()) &&
                activityRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("Activity name already exists");
        }

        ActivityMapper.updateEntity(activity, requestDto);

        // Handle pathway assignments
        return getActivityResponseDto(requestDto, activity);
    }

    @Transactional
    public void delete(UUID id) {
        if (!activityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Activity not found with id: " + id);
        }
        activityRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> searchActivities(
            AccessLevel accessLevel,
            AccessType accessType,
            ActivityCategory category,
            Pageable pageable) {
        return activityRepository.findByFilters(accessLevel, accessType, category, pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> findPrescheduledActivities(Pageable pageable) {
        return activityRepository.findByIsPrescheduled(true, pageable)
                .map(ActivityMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ActivityResponseDto> findByDeliveryMode(DeliveryMode deliveryMode, Pageable pageable) {
        return activityRepository.findByDeliveryMode(deliveryMode, pageable)
                .map(ActivityMapper::toResponse);
    }



}