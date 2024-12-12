package wi.roger.rogerWI.controller.Questionnaire;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.roger.rogerWI.DTO.Questionnaire.*;
import wi.roger.rogerWI.service.Questionnaire.QuestionnaireValidationService;
import wi.roger.rogerWI.service.QuestionnaireServiceV2;

import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questionnaire")
@RequiredArgsConstructor
@Tag(name = "Questionnaire", description = "Questionnaire management APIs")
public class QuestionnaireControllerV2 {
    private final QuestionnaireServiceV2 questionnaireService;

    @Operation(summary = "Start new questionnaire session")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully started questionnaire"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "429", description = "Too many requests")
    })
    @PostMapping("/start")
    public ResponseEntity<QuestionnaireSessionDto> startQuestionnaire(
            @Valid @RequestBody QuestionnaireStartDto request) {
        return ResponseEntity.ok(questionnaireService.startSession(request));
    }

    @Operation(summary = "Submit questionnaire responses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully submitted responses"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @PostMapping("/{sessionId}/submit")
    public ResponseEntity<QuestionnaireSessionDto> submitResponses(
            @PathVariable UUID sessionId,
            @Valid @RequestBody QuestionnaireSubmissionDto submission) {
        return ResponseEntity.ok(questionnaireService.submitResponses(sessionId, submission));
    }

    @Operation(summary = "Get questionnaire session status")
    @GetMapping("/{sessionId}/status")
    public ResponseEntity<QuestionnaireSessionDto> getSessionStatus(
            @PathVariable UUID sessionId) {
        return ResponseEntity.ok(questionnaireService.getSessionStatus(sessionId));
    }

//    @Operation(summary = "Get questionnaire response")
//    @GetMapping("/responses/{id}")
//    public ResponseEntity<QuestionnaireResponseDto> getResponse(
//            @PathVariable UUID id) {
//        return ResponseEntity.ok(questionnaireService.getResponse(id));
//    }
//
//    @Operation(summary = "Get user's questionnaire responses")
//    @GetMapping("/responses/user/{userId}")
//    public ResponseEntity<Page<QuestionnaireResponseDto>> getUserResponses(
//            @PathVariable UUID userId,
//            @Parameter(description = "Pagination information") Pageable pageable) {
//        return ResponseEntity.ok(questionnaireService.getUserResponses(userId, pageable));
//    }
}

@RestController
@RequestMapping("/api/v1/questionnaire/validation")
@RequiredArgsConstructor
@Tag(name = "Questionnaire Validation", description = "Questionnaire validation APIs")
class QuestionnaireValidationController {
    private final QuestionnaireValidationService validationService;

    @Operation(summary = "Validate questionnaire responses")
    @PostMapping("/validate")
    public ResponseEntity<ValidationResultDto> validateResponses(
            @RequestParam UUID sessionId,
            @RequestBody Map<String, Object> responses) {
        return ResponseEntity.ok(validationService.validateResponses(sessionId, responses));
    }
}



