package wi.inspire.InspireWI.service.Questionnaire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wi.inspire.InspireWI.DTO.Questionnaire.OptionDto;
import wi.inspire.InspireWI.DTO.Questionnaire.QuestionDto;
import wi.inspire.InspireWI.model.QuestionnaireFlow;
import wi.inspire.InspireWI.types.CommonEnums.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class QuestionResolver {
    public List<QuestionDto> getNextQuestions(QuestionnaireFlow flow) {
        String currentStep = flow.getCurrentStep();
        Map<String, String> responses = flow.getTemporaryResponses();

        switch (currentStep) {
            case "USER_TYPE":
                return getUserTypeQuestions(responses);
            case "ACTIVITY_TYPE":
                return getActivityTypeQuestions(responses);
            case "GRADE_INFO":
                return getGradeQuestions(responses);
            case "SCHOOL_INFO":
                return getSchoolQuestions(responses);
            case "ACTIVITY_DETAILS":
                return getActivityDetailsQuestions(responses);
            case "CONTACT_INFO":
                return getContactQuestions(responses);
            case "CONSENT":
                return getConsentQuestions(responses);
            default:
                return Collections.emptyList();
        }
    }

    private List<QuestionDto> getGradeQuestions(Map<String, String> responses) {
        return List.of(
                QuestionDto.builder()
                        .id(UUID.fromString("grade"))
                        .text("What grade are you in?")
                        .type(QuestionType.SINGLE_CHOICE)
                        .options(getGradeLevelOptions())
                        .required(true)
                        .build()
        );
    }

    private List<QuestionDto> getSchoolQuestions(Map<String, String> responses) {
        return List.of(
                QuestionDto.builder()
                        .id(UUID.fromString("school"))
                        .text("What is your school?")
                        .type(QuestionType.SCHOOL_SELECT)
                        .required(true)
                        .build()
        );
    }

    private List<QuestionDto> getActivityDetailsQuestions(Map<String, String> responses) {
        return Arrays.asList(
                QuestionDto.builder()
                        .id(UUID.fromString("numberOfParticipants"))
                        .text("How many students will participate?")
                        .type(QuestionType.TEXT)
                        .required(true)
                        .build(),
                QuestionDto.builder()
                        .id(UUID.fromString("preferredDate"))
                        .text("What date would you prefer?")
                        .type(QuestionType.DATE)
                        .required(true)
                        .build()
                // Add more questions as needed
        );
    }

    private List<QuestionDto> getContactQuestions(Map<String, String> responses) {
        return Arrays.asList(
                QuestionDto.builder()
                        .id(UUID.fromString("phoneNumber"))
                        .text("Phone Number")
                        .type(QuestionType.PHONE)
                        .required(true)
                        .build(),
                QuestionDto.builder()
                        .id(UUID.fromString("phoneType"))
                        .text("Phone Type")
                        .type(QuestionType.SINGLE_CHOICE)
                        .options(getPhoneTypeOptions())
                        .required(true)
                        .build()
        );
    }

    private List<QuestionDto> getConsentQuestions(Map<String, String> responses) {
        return List.of(
                QuestionDto.builder()
                        .id(UUID.fromString("consent"))
                        .text("Do you agree to the terms?")
                        .type(QuestionType.SINGLE_CHOICE)
                        .options(getYesNoOptions())
                        .required(true)
                        .build()
        );
    }

    private List<OptionDto> getGradeLevelOptions() {
        return Arrays.asList(
                new OptionDto("6", "6"),
                new OptionDto("7", "7"),
                new OptionDto("8", "8"),
                new OptionDto("9", "9"),
                new OptionDto("10", "10"),
                new OptionDto("11", "11"),
                new OptionDto("12", "12"),
                new OptionDto("COLLEGE", "College/Post-Secondary")
        );
    }

    private List<OptionDto> getSchoolLevelOptions() {
        return Arrays.asList(
                new OptionDto("MIDDLE_SCHOOL", "Middle School"),
                new OptionDto("HIGH_SCHOOL", "High School"),
                new OptionDto("POST_SECONDARY", "Post Secondary")
        );
    }

    private List<OptionDto> getStudentActivityOptions() {
        return Arrays.asList(
                new OptionDto("REQUEST_ACTIVITY", "Request an activity"),
                new OptionDto("RECORD_ACTIVITY", "Record an activity")
        );
    }

    private List<OptionDto> getEducatorActivityOptions() {
        return Arrays.asList(
                new OptionDto("REQUEST_ACTIVITY", "Request an activity"),
                new OptionDto("RECORD_ACTIVITY", "Record an activity")
        );
    }

    private List<OptionDto> getPhoneTypeOptions() {
        return Arrays.asList(
                new OptionDto("CELL", "Cell Phone"),
                new OptionDto("HOME", "Home Phone"),
                new OptionDto("SCHOOL", "School Phone")
        );
    }

    private List<OptionDto> getYesNoOptions() {
        return Arrays.asList(
                new OptionDto("true", "Yes"),
                new OptionDto("false", "No")
        );
    }

    private List<QuestionDto> getUserTypeQuestions(Map<String, String> responses) {
        return List.of(
                QuestionDto.builder()
                        .id(UUID.fromString("userType"))
                        .text("Are you an educator or a student?")
                        .type(QuestionType.SINGLE_CHOICE)
                        .options(Arrays.asList(
                                new OptionDto("EDUCATOR", "Educator"),
                                new OptionDto("STUDENT", "Student")
                        ))
                        .required(true)
                        .build()
        );
    }

    private List<QuestionDto> getActivityTypeQuestions(Map<String, String> responses) {
        String userType = responses.get("userType");
        if ("STUDENT".equals(userType)) {
            return List.of(
                    QuestionDto.builder()
                            .id(UUID.fromString("activityType"))
                            .text("How can Inspire help you today?")
                            .type(QuestionType.SINGLE_CHOICE)
                            .options(getStudentActivityOptions())
                            .required(true)
                            .build()
            );
        } else {
            return List.of(
                    QuestionDto.builder()
                            .id(UUID.fromString("activityType"))
                            .text("How can Inspire help you today?")
                            .type(QuestionType.SINGLE_CHOICE)
                            .options(getEducatorActivityOptions())
                            .required(true)
                            .build()
            );
        }
    }

    public boolean isComplete(QuestionnaireFlow flow) {
        String currentStep = flow.getCurrentStep();
        Map<String, String> responses = flow.getTemporaryResponses();

        switch (currentStep) {
            case "CONSENT":
                return hasRequiredConsent(responses);
            case "CONTACT_INFO":
                return hasRequiredContactInfo(responses);
            default:
                return false;
        }
    }

    private boolean hasRequiredConsent(Map<String, String> responses) {
        return responses.containsKey("waiverAccepted") &&
                Boolean.parseBoolean(responses.get("waiverAccepted"));
    }

    private boolean hasRequiredContactInfo(Map<String, String> responses) {
        return responses.containsKey("email") &&
                responses.containsKey("name") &&
                responses.containsKey("phoneNumber");
    }

}