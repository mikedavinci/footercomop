package wi.roger.rogerWI.service.Questionnaire;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wi.roger.rogerWI.model.Activity;
import wi.roger.rogerWI.model.ActivityRequest;
import wi.roger.rogerWI.model.QuestionnaireFlow;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.service.Activity.ActivityService;
import wi.roger.rogerWI.service.Activity.ActivityRequestService;

import wi.roger.rogerWI.service.School.SchoolService;
import wi.roger.rogerWI.service.User.UserService;
import wi.roger.rogerWI.types.CommonEnums.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionnaireProcessingService {
    private final UserService userService;
    private final ActivityService activityService;
    private final SchoolService schoolService;

    @Transactional
    public ActivityRequest processActivityRequest(QuestionnaireFlow flow, User user) {
        Map<String, String> responses = flow.getTemporaryResponses();

        ActivityRequest request = new ActivityRequest();
        request.setRequestor(user);

        // Set activity
        if (responses.containsKey("activityId")) {
            UUID activityId = UUID.fromString(responses.get("activityId"));
            Activity activity = activityService.getActivityEntity(activityId);  // Use this method instead
            request.setActivity(activity);
        }

        // Set dates and times
        if (responses.containsKey("preferredDate")) {
            request.setPreferredDate(LocalDate.parse(responses.get("preferredDate")));
        }
        if (responses.containsKey("startTime")) {
            request.setStartTime(LocalTime.parse(responses.get("startTime")));
        }
        if (responses.containsKey("endTime")) {
            request.setEndTime(LocalTime.parse(responses.get("endTime")));
        }

        // Set other fields
        if (responses.containsKey("numberOfParticipants")) {
            request.setNumberOfParticipants(
                    Integer.parseInt(responses.get("numberOfParticipants"))
            );
        }

        // Process grade levels if present
        if (responses.containsKey("participantGrades")) {
            Set<GradeLevel> grades = parseGradeLevels(responses.get("participantGrades"));
            request.setParticipantGrades(grades);
        }

        // Save the request
        return activityService.saveActivityRequest(request);
    }

    private Set<GradeLevel> parseGradeLevels(String gradesString) {
        return Arrays.stream(gradesString.split(","))
                .map(String::trim)
                .map(GradeLevel::valueOf)
                .collect(Collectors.toSet());
    }
}