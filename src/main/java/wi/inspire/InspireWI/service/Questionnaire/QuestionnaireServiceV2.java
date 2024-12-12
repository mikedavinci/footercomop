package wi.roger.rogerWI.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.Questionnaire.*;
import wi.roger.rogerWI.service.ResourceExceptions.*;
import wi.roger.rogerWI.model.*;
import wi.roger.rogerWI.repository.*;
import wi.roger.rogerWI.service.Questionnaire.QuestionResolver;
import wi.roger.rogerWI.service.Questionnaire.QuestionnaireSecurityService;
import wi.roger.rogerWI.service.Questionnaire.QuestionnaireValidationService;
import wi.roger.rogerWI.types.CommonEnums;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestionnaireServiceV2 {
    private final QuestionnaireFlowRepository flowRepository;
    private final QuestionnaireResponseRepository responseRepository;
    private final UserRepository userRepository;
    private final ActivityRequestRepository activityRequestRepository;
    private final ActivityRecordRepository activityRecordRepository;
    private final QuestionnaireValidationService validationService;
    private final QuestionnaireSecurityService securityService;
    private final QuestionResolver questionResolver;
    private final ActivityRepository activityRepository;

    @Transactional
    public QuestionnaireSessionDto startSession(QuestionnaireStartDto request) {
        // Validate request
        securityService.validateRequest(request);

        // Create new flow
        QuestionnaireFlow flow = new QuestionnaireFlow();
        flow.setCurrentStep("USER_TYPE");
        flow.getTemporaryResponses().put("userType", request.getUserType());
        flow.getTemporaryResponses().put("email", request.getEmail());

        flow = flowRepository.save(flow);

        // Get initial questions
        List<QuestionDto> nextQuestions = questionResolver.getNextQuestions(flow);

        return QuestionnaireSessionDto.builder()
                .sessionId(flow.getId())
                .currentStep(flow.getCurrentStep())
                .nextQuestions(nextQuestions)
                .completed(false)
                .lastUpdated(flow.getUpdatedDate())
                .build();
    }

    @Transactional
    public QuestionnaireSessionDto submitResponses(UUID sessionId, QuestionnaireSubmissionDto submission) {
        QuestionnaireFlow flow = flowRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceExceptions.ResourceNotFoundException("Session not found"));

        // Validate submission
        securityService.validateSubmission(submission);
        validationService.validateResponses(flow, submission.getResponses());

        // Update flow with new responses
        updateFlowResponses(flow, submission.getResponses());

        // Check if questionnaire is complete
        if (isQuestionnaireComplete(flow)) {
            return processCompletedQuestionnaire(flow);
        }

        // Get next questions
        List<QuestionDto> nextQuestions = questionResolver.getNextQuestions(flow);

        return QuestionnaireSessionDto.builder()
                .sessionId(flow.getId())
                .currentStep(flow.getCurrentStep())
                .nextQuestions(nextQuestions)
                .completed(false)
                .lastUpdated(flow.getUpdatedDate())
                .build();
    }

    private void updateFlowResponses(QuestionnaireFlow flow, Map<String, Object> responses) {
        responses.forEach((key, value) ->
                flow.getTemporaryResponses().put(key, value.toString())
        );
        flowRepository.save(flow);
    }

    private boolean isQuestionnaireComplete(QuestionnaireFlow flow) {
        return questionResolver.isComplete(flow);
    }

    private QuestionnaireSessionDto processCompletedQuestionnaire(QuestionnaireFlow flow) {
        // Create or update user
        User user = createOrUpdateUser(flow);

        // Create activity request or record
        if (isActivityRequest(flow)) {
            ActivityRequest request = createActivityRequest(flow, user);
            saveQuestionnaireResponse(flow, user, request, null);
        } else {
            ActivityRecord record = createActivityRecord(flow, user);
            saveQuestionnaireResponse(flow, user, null, record);
        }

        flow.setCompleted(true);
        flowRepository.save(flow);

        return QuestionnaireSessionDto.builder()
                .sessionId(flow.getId())
                .completed(true)
                .lastUpdated(flow.getUpdatedDate())
                .build();
    }

    private User createOrUpdateUser(QuestionnaireFlow flow) {
        Map<String, String> responses = flow.getTemporaryResponses();

        return userRepository.findByEmail(responses.get("email"))
                .map(user -> updateUser(user, responses))
                .orElseGet(() -> createUser(responses));
    }

    private void saveQuestionnaireResponse(QuestionnaireFlow flow, User user,
                                           ActivityRequest request, ActivityRecord record) {
        QuestionnaireResponse response = new QuestionnaireResponse();
        response.setUser(user);
        response.setActivityRequest(request);
        response.setActivityRecord(record);
        response.setRawResponses(flow.getTemporaryResponses());
        response.setIpAddress(flow.getIpAddress());
        response.setUserAgent(flow.getUserAgent());

        responseRepository.save(response);
    }

    private boolean isActivityRequest(QuestionnaireFlow flow) {
        return "REQUEST_ACTIVITY".equals(flow.getTemporaryResponses().get("activityType"));
    }

    private User createUser(Map<String, String> responses) {
        User user = new User();
        user.setEmail(responses.get("email"));
        user.setName(responses.get("name"));
        user.setUserType(CommonEnums.UserType.valueOf(responses.get("userType")));

        if (responses.containsKey("phoneNumber")) {
            user.setPhoneNumber(responses.get("phoneNumber"));
        }
        if (responses.containsKey("phoneType")) {
            user.setPhoneType(CommonEnums.PhoneType.valueOf(responses.get("phoneType")));
        }
        if (responses.containsKey("smsConsent")) {
            user.setSmsConsent(Boolean.valueOf(responses.get("smsConsent")));
        }

        return userRepository.save(user);
    }

    private User updateUser(User user, Map<String, String> responses) {
        if (responses.containsKey("name")) {
            user.setName(responses.get("name"));
        }
        if (responses.containsKey("phoneNumber")) {
            user.setPhoneNumber(responses.get("phoneNumber"));
        }
        if (responses.containsKey("phoneType")) {
            user.setPhoneType(CommonEnums.PhoneType.valueOf(responses.get("phoneType")));
        }
        if (responses.containsKey("smsConsent")) {
            user.setSmsConsent(Boolean.valueOf(responses.get("smsConsent")));
        }

        return userRepository.save(user);
    }

    private ActivityRequest createActivityRequest(QuestionnaireFlow flow, User user) {
        Map<String, String> responses = flow.getTemporaryResponses();

        ActivityRequest request = new ActivityRequest();
        request.setRequestor(user);

        // Set activity
        if (responses.containsKey("activityId")) {
            Activity activity = activityRepository.findById(UUID.fromString(responses.get("activityId")))
                    .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
            request.setActivity(activity);
        }

        // Set other fields from responses
        if (responses.containsKey("preferredDate")) {
            request.setPreferredDate(LocalDate.parse(responses.get("preferredDate")));
        }
        if (responses.containsKey("startTime")) {
            request.setStartTime(LocalTime.parse(responses.get("startTime")));
        }
        // ... set other fields as needed

        return activityRequestRepository.save(request);
    }

    private ActivityRecord createActivityRecord(QuestionnaireFlow flow, User user) {
        Map<String, String> responses = flow.getTemporaryResponses();

        ActivityRecord record = new ActivityRecord();
        record.setRecorder(user);

        if (responses.containsKey("activityId")) {
            Activity activity = activityRepository.findById(UUID.fromString(responses.get("activityId")))
                    .orElseThrow(() -> new ResourceNotFoundException("Activity not found"));
            record.setActivity(activity);
        }

        if (responses.containsKey("activityDate")) {
            record.setActivityDate(LocalDate.parse(responses.get("activityDate")));
        }
        if (responses.containsKey("numberOfParticipants")) {
            record.setNumberOfParticipants(Integer.parseInt(responses.get("numberOfParticipants")));
        }

        // ... set other fields as needed

        return activityRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public QuestionnaireSessionDto getSessionStatus(UUID sessionId) {
        QuestionnaireFlow flow = flowRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        return QuestionnaireSessionDto.builder()
                .sessionId(flow.getId())
                .currentStep(flow.getCurrentStep())
                .nextQuestions(questionResolver.getNextQuestions(flow))
                .completed(flow.isCompleted())
                .lastUpdated(flow.getUpdatedDate())
                .build();
    }

//    @Transactional(readOnly = true)
//    public QuestionnaireResponseDto getResponse(UUID id) {
//        QuestionnaireResponse response = responseRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Response not found"));
//
//        return mapToResponseDto(response);
//    }

//    @Transactional(readOnly = true)
//    public Page<QuestionnaireResponseDto> getUserResponses(UUID userId, Pageable pageable) {
//        return responseRepository.findByUserId(userId, pageable)
//                .map(this::mapToResponseDto);
//    }

//    private QuestionnaireResponseDto mapToResponseDto(QuestionnaireResponse response) {
//        return QuestionnaireResponseDto.builder()
//                .id(response.getId())
//                .userId(response.getUser().getId())
//                .responses(new HashMap<>(response.getRawResponses()))
//                .activityRequestId(response.getActivityRequest() != null ?
//                        response.getActivityRequest().getId() : null)
//                .activityRecordId(response.getActivityRecord() != null ?
//                        response.getActivityRecord().getId() : null)
//                .submittedDate(response.getSubmittedDate())
//                .build();
//    }
}