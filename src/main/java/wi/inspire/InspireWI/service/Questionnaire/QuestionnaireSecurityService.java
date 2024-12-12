package wi.roger.rogerWI.service.Questionnaire;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wi.roger.rogerWI.DTO.Questionnaire.QuestionnaireStartDto;
import wi.roger.rogerWI.DTO.Questionnaire.QuestionnaireSubmissionDto;
import wi.roger.rogerWI.service.ResourceExceptions;

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