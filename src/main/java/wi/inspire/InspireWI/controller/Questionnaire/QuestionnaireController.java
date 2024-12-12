package wi.inspire.InspireWI.controller.Questionnaire;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.inspire.InspireWI.DTO.Questionnaire.FormSubmissionDto;
import wi.inspire.InspireWI.DTO.Questionnaire.QuestionnaireResponseDto;
import wi.inspire.InspireWI.service.Questionnaire.QuestionnaireService;

import javax.validation.Valid;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/questionnaire")
@RequiredArgsConstructor
@Tag(name = "Questionnaire", description = "Questionnaire submission APIs")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    @Operation(summary = "Submit questionnaire")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully submitted questionnaire"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/submit")
    public ResponseEntity<QuestionnaireResponseDto> submitQuestionnaire(
            @Valid @RequestBody FormSubmissionDto submission) {
        return ResponseEntity.ok(questionnaireService.processSubmission(submission));
    }

    @Operation(summary = "Get questionnaire submissions by user")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<QuestionnaireResponseDto>> getUserSubmissions(
            @PathVariable UUID userId) {
        return ResponseEntity.ok(questionnaireService.getUserSubmissions(userId));
    }

    @Operation(summary = "Get image from submission")
    @GetMapping("/{submissionId}/image")
    public ResponseEntity<byte[]> getSubmissionImage(@PathVariable UUID submissionId) {
        return questionnaireService.getSubmissionImage(submissionId);
    }

    @Operation(summary = "Decode base64 image")
    @PostMapping("/decode")
    public ResponseEntity<byte[]> decodeBase64ToImage(@RequestBody String base64String) {
        try {
            // Remove potential "data:image/jpeg;base64," prefix
            String base64Data = base64String;
            if (base64String.contains(",")) {
                base64Data = base64String.split(",")[1];
            }

            // Decode the Base64 string into binary data
            byte[] binaryData = Base64.getDecoder().decode(base64Data);

            // Return the binary data with the appropriate content type
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE)
                    .body(binaryData);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}