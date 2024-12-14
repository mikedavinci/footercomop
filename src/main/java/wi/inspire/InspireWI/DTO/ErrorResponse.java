package wi.roger.rogerWI.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response object")
public class ErrorResponse {
    @Schema(description = "Error message", example = "Invalid credentials")
    private String message;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error timestamp", example = "2024-03-15T10:30:00Z")
    private LocalDateTime timestamp;

    // Convenient constructor for quick error creation
    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}