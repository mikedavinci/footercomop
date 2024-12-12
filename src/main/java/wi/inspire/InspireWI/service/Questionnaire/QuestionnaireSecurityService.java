package wi.inspire.InspireWI.service.Questionnaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wi.inspire.InspireWI.DTO.Questionnaire.QuestionnaireStartDto;
import wi.inspire.InspireWI.DTO.Questionnaire.QuestionnaireSubmissionDto;
import wi.inspire.InspireWI.service.ResourceExceptions;

@Service
@RequiredArgsConstructor
public class QuestionnaireSecurityService {
    public void validateRequest(QuestionnaireStartDto request) {
        validateEmail(request.getEmail());
    }

    public void validateSubmission(QuestionnaireSubmissionDto submission) {
        // Add any submission validation logic here
    }

    private void validateEmail(String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ResourceExceptions.ValidationException("Invalid email format");
        }
    }
}