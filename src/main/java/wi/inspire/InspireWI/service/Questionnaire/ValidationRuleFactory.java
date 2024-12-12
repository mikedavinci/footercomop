package wi.inspire.InspireWI.service.Questionnaire;

import org.springframework.stereotype.Component;
import wi.inspire.InspireWI.service.ResourceExceptions;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidationRuleFactory {
    private final Map<String, ValidationRule> rules;

    public ValidationRuleFactory() {
        rules = new HashMap<>();
        rules.put("email", value -> {
            if (value == null || !value.toString().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new ResourceExceptions.ValidationException("Invalid email format");
            }
        });
        rules.put("phone", value -> {
            if (value == null || !value.toString().matches("^\\+?[1-9]\\d{1,14}$")) {
                throw new ResourceExceptions.ValidationException("Invalid phone number format");
            }
        });
        // Add more validation rules as needed
    }

    public ValidationRule getRule(String key) {
        return rules.get(key);
    }
}