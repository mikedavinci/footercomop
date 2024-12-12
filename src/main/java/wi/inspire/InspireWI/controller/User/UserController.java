// User Controller Endpoints
// GET    /api/users                - Get all users (paginated)
// GET    /api/users/{id}           - Get user by ID
// POST   /api/users                - Create new user
// PUT    /api/users/{id}           - Update existing user
// DELETE /api/users/{id}           - Delete user
// GET    /api/users/search         - Search users by email/name

package wi.inspire.InspireWI.controller.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import wi.inspire.InspireWI.DTO.User.*;
import wi.inspire.InspireWI.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {
    private final UserService userService;

    // GET    /api/users - Get all users (paginated)
    @Operation(summary = "Get all users",
            description = "Returns a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<UserListResponseDto>> getAllUsers(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    // GET    /api/users/{id} - Get user by ID
    @Operation(summary = "Get user by ID",
            description = "Returns a single user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // POST   /api/users - Create new user
    @Operation(summary = "Create new user",
            description = "Creates a new user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created user"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Parameter(description = "User details") @Valid @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.create(requestDto));
    }

    // PUT    /api/users/{id} - Update existing user
    @Operation(summary = "Update existing user",
            description = "Updates an existing user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable UUID id,
            @Parameter(description = "Updated user details") @Valid @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.update(id, requestDto));
    }

    // DELETE /api/users/{id} - Delete user
    @Operation(summary = "Delete user",
            description = "Deletes an existing user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET    /api/users/search - Search users by email/name
    @Operation(summary = "Search users",
            description = "Search users by email and/or name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<UserListResponseDto>> searchUsers(
            @Parameter(description = "Email to search for (optional)")
            @RequestParam(required = false) String email,
            @Parameter(description = "Name to search for (optional)")
            @RequestParam(required = false) String name,
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return ResponseEntity.ok(userService.searchUsers(email, name, pageable));
    }

    // GET    /api/users/educators - Get all educators
    @Operation(summary = "Get all educators")
    @GetMapping("/educators")
    public ResponseEntity<Page<UserListResponseDto>> getAllEducators(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllEducators(pageable));
    }

    // GET    /api/users/students - Get all students
    @Operation(summary = "Get all students")
    @GetMapping("/students")
    public ResponseEntity<Page<UserListResponseDto>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllStudents(pageable));
    }

    // PATCH  /api/users/{id}/consent - Update user SMS consent
    @Operation(summary = "Update user SMS consent")
    @PatchMapping("/{id}/consent")
    public ResponseEntity<UserResponseDto> updateSmsConsent(
            @PathVariable UUID id,
            @RequestParam Boolean consent) {
        return ResponseEntity.ok(userService.updateSmsConsent(id, consent));
    }

}