package wi.roger.rogerWI.service.Questionnaire;

import wi.roger.rogerWI.service.ResourceExceptions;

public interface ValidationRule {
    void validate(Object value) throws ResourceExceptions.ValidationException;
}