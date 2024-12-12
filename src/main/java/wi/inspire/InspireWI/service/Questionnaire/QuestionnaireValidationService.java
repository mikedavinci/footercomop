package wi.roger.rogerWI.service.Questionnaire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wi.roger.rogerWI.DTO.Questionnaire.ValidationResultDto;
import wi.roger.rogerWI.model.*;
import wi.roger.rogerWI.repository.QuestionnaireFlowRepository;
import wi.roger.rogerWI.service.ResourceExceptions;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestionnaireValidationService {
    private final QuestionnaireFlowRepository flowRepository;


    public void validateResponses(QuestionnaireFlow flow, Map<String, Object> responses) {
        // Validate each response
        responses.forEach((key, value) -> validateResponse(key, value));

        // Validate dependencies
        validateDependencies(flow, responses);

        // Validate required fields
        validateRequiredFields(flow, responses);
    }

    private void validateResponse(String key, Object value) {
        // Basic validation based on key
        switch(key) {
            case "email":
                validateEmail(value);
                break;
            case "phoneNumber":
                validatePhone(value);
                break;
            // Add more cases as needed
        }
    }

    private void validateDependencies(QuestionnaireFlow flow, Map<String, Object> responses) {
        Map<String, String> allResponses = new HashMap<>(flow.getTemporaryResponses());
        responses.forEach((key, value) -> allResponses.put(key, value.toString()));

        String currentStep = flow.getCurrentStep();
        switch (currentStep) {
            case "SCHOOL_INFO":
                validateSchoolDependencies(allResponses);
                break;
            case "ACTIVITY_DETAILS":
                validateActivityDependencies(allResponses);
                break;
        }
    }

    private void validateRequiredFields(QuestionnaireFlow flow, Map<String, Object> responses) {
        List<String> requiredFields = getRequiredFields(flow.getCurrentStep());
        for (String field : requiredFields) {
            if (!responses.containsKey(field) && !flow.getTemporaryResponses().containsKey(field)) {
                throw new ValidationException("Required field missing: " + field);
            }
        }
    }

    private void validateEmail(Object value) {
        if (value == null || !value.toString().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ValidationException("Invalid email format");
        }
    }

    private void validatePhone(Object value) {
        if (value == null || !value.toString().matches("^\\+?[1-9]\\d{1,14}$")) {
            throw new ValidationException("Invalid phone number format");
        }
    }

    private void validateSchoolDependencies(Map<String, String> responses) {
        String userType = responses.get("userType");
        if ("STUDENT".equals(userType) && !responses.containsKey("schoolId")) {
            throw new ValidationException("School is required for students");
        }
    }

    private void validateActivityDependencies(Map<String, String> responses) {
        String activityType = responses.get("activityType");
        if ("REQUEST_ACTIVITY".equals(activityType)) {
            if (!responses.containsKey("preferredDate")) {
                throw new ValidationException("Preferred date is required for activity requests");
            }
        }
    }

    private List<String> getRequiredFields(String step) {
        switch (step) {
            case "USER_TYPE":
                return Arrays.asList("userType", "email");
            case "SCHOOL_INFO":
                return Collections.singletonList("schoolId");
            case "ACTIVITY_DETAILS":
                return Collections.singletonList("activityId");
            default:
                return Collections.emptyList();
        }
    }

    public ValidationResultDto validateResponses(UUID sessionId, Map<String, Object> responses) {
        QuestionnaireFlow flow = flowRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceExceptions.ResourceNotFoundException("Session not found"));

        try {
            validateResponses(flow, responses);
            return ValidationResultDto.builder()
                    .valid(true)
                    .build();
        } catch (ValidationException e) {
            return ValidationResultDto.builder()
                    .valid(false)
                    .errors(e.getErrors())
                    .build();
        }
    }

}