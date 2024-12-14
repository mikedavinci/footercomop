// controller/AuthController.java
package wi.roger.rogerWI.controller.Auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wi.roger.rogerWI.DTO.Auth.AuthResponse;
import wi.roger.rogerWI.DTO.Auth.LoginRequest;
import wi.roger.rogerWI.DTO.Auth.RegisterRequest;
import wi.roger.rogerWI.DTO.ErrorResponse;
import wi.roger.rogerWI.DTO.User.Auth.AuthResponseDto;
import wi.roger.rogerWI.service.Auth.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Login user",
            description = "Authenticates a user and returns JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully authenticated",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @Parameter(description = "Login credentials", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    // POST   /api/auth/register - Register new user
    @Operation(summary = "Register new user",
            description = "Register a new user and return JWT tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully registered",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409",
                    description = "Email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @Parameter(description = "Registration details", required = true)
            @Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @Operation(summary = "Logout user",
            description = "Invalidates the refresh token and logs out the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully logged out"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid refresh token",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @Parameter(description = "Refresh token to invalidate", required = true)
            @RequestParam String refreshToken) {
        authService.logout(refreshToken);
        return ResponseEntity.ok().build();
    }
}