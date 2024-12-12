package wi.inspire.InspireWI.service.Questionnaire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import wi.inspire.InspireWI.DTO.Questionnaire.FormSubmissionDto;
import wi.inspire.InspireWI.DTO.Questionnaire.QuestionnaireResponseDto;
import wi.inspire.InspireWI.mapper.ActivityMapper;
import wi.inspire.InspireWI.mapper.UserMapper;
import wi.inspire.InspireWI.model.*;
import wi.inspire.InspireWI.repository.*;
import wi.inspire.InspireWI.service.ResourceExceptions.*;
import wi.inspire.InspireWI.types.CommonEnums.*;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionnaireService {
    private final QuestionnaireSubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final ActivityRequestRepository activityRequestRepository;
    private final ActivityRecordRepository activityRecordRepository;
    private final ObjectMapper objectMapper;

    public QuestionnaireResponseDto processSubmission(FormSubmissionDto submission) {
        // Create or update user
        User user = createOrUpdateUser(submission);

        // Create activity request or record based on type
        if ("REQUEST".equals(submission.getActivityType())) {
            ActivityRequest activityRequest = createActivityRequest(submission, user);
            return saveSubmission(submission, user, activityRequest, null);
        } else if ("RECORD".equals(submission.getActivityType())) {
            ActivityRecord activityRecord = createActivityRecord(submission, user);
            return saveSubmission(submission, user, null, activityRecord);
        } else {
            throw new ValidationException("Invalid activity type");
        }
    }

    private User createOrUpdateUser(FormSubmissionDto submission) {
        return userRepository.findByEmail(submission.getEmail())
                .map(user -> updateUser(user, submission))
                .orElseGet(() -> createUser(submission));
    }

    private User createUser(FormSubmissionDto submission) {
        User user = new User();
        user.setName(submission.getName());
        user.setEmail(submission.getEmail());
        user.setUserType(UserType.valueOf(submission.getUserType()));
        user.setPhoneNumber(submission.getPhoneNumber());

        if (submission.getSchoolId() != null) {
            // Set school if provided
            School school = schoolRepository.findById(submission.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("School not found"));
            user.setSchool(school);
        }

        return userRepository.save(user);
    }

    private User updateUser(User user, FormSubmissionDto submission) {
        user.setName(submission.getName());
        user.setPhoneNumber(submission.getPhoneNumber());
        return userRepository.save(user);
    }

    private ActivityRequest createActivityRequest(FormSubmissionDto submission, User user) {
        ActivityRequest request = new ActivityRequest();
        request.setRequestor(user);
        request.setPreferredDate(submission.getPreferredDate());
        request.setStartTime(submission.getStartTime());
        request.setEndTime(submission.getEndTime());
        request.setNumberOfParticipants(submission.getNumberOfParticipants());
        request.setObjectives(submission.getObjectives());
        request.setWaiverAccepted(submission.getWaiverAccepted());

        if (submission.getParticipantGrades() != null) {
            request.setParticipantGrades(submission.getParticipantGrades().stream()
                    .map(GradeLevel::valueOf)
                    .collect(Collectors.toSet()));
        }

        return activityRequestRepository.save(request);
    }

    private ActivityRecord createActivityRecord(FormSubmissionDto submission, User user) {
        ActivityRecord record = new ActivityRecord();
        record.setRecorder(user);
        record.setNumberOfParticipants(submission.getNumberOfParticipants());

        if (submission.getParticipantGrades() != null) {
            record.setParticipantGrades(submission.getParticipantGrades().stream()
                    .map(GradeLevel::valueOf)
                    .collect(Collectors.toSet()));
        }

        return activityRecordRepository.save(record);
    }

    private QuestionnaireResponseDto saveSubmission(
            FormSubmissionDto submissionDto,
            User user,
            ActivityRequest activityRequest,
            ActivityRecord activityRecord) {

        QuestionnaireSubmission submission = new QuestionnaireSubmission();
        submission.setUser(user);
        submission.setActivityRequest(activityRequest);
        submission.setActivityRecord(activityRecord);
        submission.setImageData(submissionDto.getImageData());

        try {
            submission.setRawData(objectMapper.writeValueAsString(submissionDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing submission data", e);
        }

        submission = submissionRepository.save(submission);
        return mapToResponseDto(submission);
    }

    public ResponseEntity<byte[]> getSubmissionImage(UUID submissionId) {
        QuestionnaireSubmission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));

        if (submission.getImageData() == null) {
            throw new ResourceNotFoundException("No image found for this submission");
        }

        // Remove potential "data:image/jpeg;base64," prefix
        String base64Data = submission.getImageData();
        if (base64Data.contains(",")) {
            base64Data = base64Data.split(",")[1];
        }

        byte[] imageBytes = Base64.getDecoder().decode(base64Data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                .body(imageBytes);
    }

    public List<QuestionnaireResponseDto> getUserSubmissions(UUID userId) {
        return submissionRepository.findByUserId(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private QuestionnaireResponseDto mapToResponseDto(QuestionnaireSubmission submission) {
        return QuestionnaireResponseDto.builder()
                .id(submission.getId())
                .user(UserMapper.toResponse(submission.getUser()))
                .activityRequest(submission.getActivityRequest() != null ?
                        ActivityMapper.toResponse(submission.getActivityRequest()) : null)
                .activityRecord(submission.getActivityRecord() != null ?
                        ActivityMapper.toResponse(submission.getActivityRecord()) : null)
                .submittedDate(submission.getSubmittedDate())
                .imageData(submission.getImageData())
                .build();
    }
}