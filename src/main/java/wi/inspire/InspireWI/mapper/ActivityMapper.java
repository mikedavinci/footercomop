package wi.roger.rogerWI.mapper;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import wi.roger.rogerWI.DTO.Activity.*;
import wi.roger.rogerWI.model.Activity;
import wi.roger.rogerWI.model.ActivityRecord;
import wi.roger.rogerWI.model.ActivityRequest;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ActivityMapper {

    public static ActivityReferenceDto toReference(Activity activity) {
        if (activity == null) return null;

        return ActivityReferenceDto.builder()
                .id(activity.getId())
                .name(activity.getName())
                .category(activity.getCategory())
                .build();
    }

    // Activity mappings
    public static ActivityResponseDto toResponse(Activity activity) {
        if (activity == null) return null;

        return ActivityResponseDto.builder()
                .id(activity.getId())
                .name(activity.getName())
                .accessLevel(activity.getAccessLevel())
                .accessTypes(activity.getAccessTypes())
                .activityType(activity.getActivityType())
                .deliveryMode(activity.getDeliveryMode())
                .category(activity.getCategory())
                .description(activity.getDescription())
                .isPrescheduled(activity.getIsPrescheduled())
                .multipleCompanyAllowed(activity.getMultipleCompanyAllowed())
                .availablePathways(activity.getAvailablePathways().stream()
                        .map(CareerPathwayMapper::toReference)
                        .collect(Collectors.toSet()))
                .createdDate(activity.getCreatedDate())
                .updatedDate(activity.getUpdatedDate())
                .build();
    }

    public static Activity toEntity(ActivityCreateDto request) {
        if (request == null) return null;

        Activity activity = new Activity();
        updateEntity(activity, request);
        return activity;
    }

    public static void updateEntity(Activity activity, ActivityCreateDto request) {
        if (activity == null || request == null) return;

        activity.setName(request.getName());
        activity.setAccessLevel(request.getAccessLevel());
        activity.setAccessTypes(request.getAccessTypes() != null ? request.getAccessTypes() : new HashSet<>());
        activity.setActivityType(request.getActivityType());
        activity.setDeliveryMode(request.getDeliveryMode());
        activity.setCategory(request.getCategory());
        activity.setDescription(request.getDescription());
        activity.setIsPrescheduled(request.getIsPrescheduled());
        activity.setMultipleCompanyAllowed(request.getMultipleCompanyAllowed());
        // Note: Pathway assignments should be handled in service layer
    }

    // Activity Request mappings
    public static ActivityRequestResponseDto toResponse(ActivityRequest request) {
        if (request == null) return null;

        return ActivityRequestResponseDto.builder()
                .id(request.getId())
                .requestor(UserMapper.toResponse(request.getRequestor()))
                .activity(toReference(request.getActivity()))
                .companies(request.getCompanies().stream()
                        .map(CompanyMapper::toReference)
                        .collect(Collectors.toSet()))
                .preferredDate(request.getPreferredDate())
                .completionDate(request.getCompletionDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .numberOfParticipants(request.getNumberOfParticipants())
                .participantGrades(request.getParticipantGrades())
                .objectives(request.getObjectives())
                .subjectAlignment(request.getSubjectAlignment())
                .avRequirements(request.getAvRequirements())
                .waiverAccepted(request.getWaiverAccepted())
                .employerNotificationAccepted(request.getEmployerNotificationAccepted())
                .speakerTopics(request.getSpeakerTopics())
                .projectStartDate(request.getProjectStartDate())
                .projectDuration(request.getProjectDuration())
                .projectCurriculum(request.getProjectCurriculum())
                .numberOfVolunteers(request.getNumberOfVolunteers())
                .volunteerNeedsDescription(request.getVolunteerNeedsDescription())
                .mentorshipGoals(request.getMentorshipGoals())
                .numberOfMentorSessions(request.getNumberOfMentorSessions())
                .careerInterestArea(request.getCareerInterestArea())
                .sessionQuestions(request.getSessionQuestions())
                .arrivalInstructions(request.getArrivalInstructions())
                .createdDate(request.getCreatedDate())
                .updatedDate(request.getUpdatedDate())
                .build();
    }

    // Changed from ActivityRequestDto
    public static ActivityRequest toEntity(ActivityRequestDto requestDto) {
        if (requestDto == null) return null;

        ActivityRequest request = new ActivityRequest();
        updateEntity(request, requestDto);
        return request;
    }

    public static void updateEntity(ActivityRequest request, ActivityRequestDto requestDto) {
        if (request == null || requestDto == null) return;

        request.setPreferredDate(requestDto.getPreferredDate());
        request.setStartTime(requestDto.getStartTime());
        request.setEndTime(requestDto.getEndTime());
        request.setNumberOfParticipants(requestDto.getNumberOfParticipants());
        request.setParticipantGrades(requestDto.getParticipantGrades());
        request.setObjectives(requestDto.getObjectives());
        request.setSubjectAlignment(requestDto.getSubjectAlignment());
        request.setAvRequirements(requestDto.getAvRequirements());
        request.setWaiverAccepted(requestDto.getWaiverAccepted());
        request.setEmployerNotificationAccepted(requestDto.getEmployerNotificationAccepted());
        request.setSpeakerTopics(requestDto.getSpeakerTopics());
        request.setProjectStartDate(requestDto.getProjectStartDate());
        request.setProjectDuration(requestDto.getProjectDuration());
        request.setProjectCurriculum(requestDto.getProjectCurriculum());
        request.setNumberOfVolunteers(requestDto.getNumberOfVolunteers());
        request.setVolunteerNeedsDescription(requestDto.getVolunteerNeedsDescription());
        request.setMentorshipGoals(requestDto.getMentorshipGoals());
        request.setNumberOfMentorSessions(requestDto.getNumberOfMentorSessions());
        request.setCareerInterestArea(requestDto.getCareerInterestArea());
        request.setSessionQuestions(requestDto.getSessionQuestions());
        request.setArrivalInstructions(requestDto.getArrivalInstructions());
        // Note: relationships (requestor, activity, companies) are handled in service layer
    }

    // Activity Record mappings
    public static ActivityRecordResponseDto toResponse(ActivityRecord record) {
        if (record == null) return null;

        return ActivityRecordResponseDto.builder()
                .id(record.getId())
                .activity(toReference(record.getActivity()))
                .recorder(UserMapper.toResponse(record.getRecorder()))
                .activityDate(record.getActivityDate())
                .numberOfParticipants(record.getNumberOfParticipants())
                .participantGrades(record.getParticipantGrades())
                .careerPathways(record.getCareerPathways().stream()
                        .map(CareerPathwayMapper::toReference)
                        .collect(Collectors.toSet()))
                .participatingCompanies(record.getParticipatingCompanies().stream()
                        .map(CompanyMapper::toReference)
                        .collect(Collectors.toSet()))
                .documentationUrl(record.getDocumentationUrl())
                .createdDate(record.getCreatedDate())
                .updatedDate(record.getUpdatedDate())
                .build();
    }

    public static ActivityRecord toEntity(ActivityRecordDto recordDto) {
        if (recordDto == null) return null;

        ActivityRecord record = new ActivityRecord();
        updateEntity(record, recordDto);
        return record;
    }

    public static void updateEntity(ActivityRecord record, ActivityRecordDto recordDto) {
        if (record == null || recordDto == null) return;

        record.setActivityDate(recordDto.getActivityDate());
        record.setNumberOfParticipants(recordDto.getNumberOfParticipants());
        record.setParticipantGrades(recordDto.getParticipantGrades());
        record.setDocumentationUrl(recordDto.getDocumentationUrl());
        // Note: relationships (activity, recorder, careerPathways, participatingCompanies) should be handled in service layer
    }

    public static ActivityRecordListDto toListResponse(ActivityRecord record) {
        if (record == null) return null;

        return ActivityRecordListDto.builder()
                .id(record.getId())
                .activity(toReference(record.getActivity()))
                .activityDate(record.getActivityDate())
                .numberOfParticipants(record.getNumberOfParticipants())
                .participatingCompanies(record.getParticipatingCompanies().stream()
                        .map(CompanyMapper::toReference)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static ActivityListDto toListResponse(Activity activity) {
        if (activity == null) return null;

        return ActivityListDto.builder()
                .id(activity.getId())
                .name(activity.getName())
                .accessLevel(activity.getAccessLevel())
                .accessTypes(activity.getAccessTypes())
                .category(activity.getCategory())
                .deliveryMode(activity.getDeliveryMode())
                .isPrescheduled(activity.getIsPrescheduled())
                .build();
    }

}