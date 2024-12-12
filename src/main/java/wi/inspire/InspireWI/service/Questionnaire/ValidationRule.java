package wi.inspire.InspireWI.service.Questionnaire;

import wi.inspire.InspireWI.service.ResourceExceptions;

public interface ValidationRule {
    void validate(Object value) throws ResourceExceptions.ValidationException;
}