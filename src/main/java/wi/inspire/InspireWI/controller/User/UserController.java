// User Controller Endpoints
// GET    /api/users                - Get all users (paginated)
// GET    /api/users/{id}           - Get user by ID
// POST   /api/users                - Create new user
// PUT    /api/users/{id}           - Update existing user
// DELETE /api/users/{id}           - Delete user
// GET    /api/users/search         - Search users by email/name

package wi.roger.rogerWI.controller.User;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import wi.roger.rogerWI.DTO.User.*;
import wi.roger.rogerWI.SecurityConfiguration.CurrentUser;
import wi.roger.rogerWI.model.School;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import wi.roger.rogerWI.types.CommonEnums.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User", description = "User management APIs")
public class UserController {
    private final UserService userService;

    // GET    /api/users - Get all users (paginated)
    // Only ADMIN can access all users
    @Operation(summary = "Get all users",
            description = "Returns a paginated list of all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users",
                    content = @Content(schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserListResponseDto>> getAllUsers(
            @Parameter(description = "Pagination information") Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    // GET    /api/users/{id} - Get user by ID
    // ADMIN or user themselves can access
    @Operation(summary = "Get user by ID",
            description = "Returns a single user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "ID of the user to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    // POST   /api/users - Create new user
    // Anyone can create a user account
    @Operation(summary = "Create new user",
            description = "Creates a new user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created user"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Parameter(description = "User details") @Valid @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.create(requestDto));
    }

    // PUT    /api/users/{id} - Update existing user
    // ADMIN or user themselves can update
    @Operation(summary = "Update existing user",
            description = "Updates an existing user with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated user"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @Parameter(description = "ID of the user to update") @PathVariable UUID id,
            @Parameter(description = "Updated user details") @Valid @RequestBody UserRequestDto requestDto) {
        return ResponseEntity.ok(userService.update(id, requestDto));
    }

    // DELETE /api/users/{id} - Delete user
    // Only ADMIN can delete users
    @Operation(summary = "Delete user",
            description = "Deletes an existing user by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted user"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET    /api/users/search - Search users by email/name
    // ADMIN or REGIONAL_LEADS can search users
    @Operation(summary = "Search users",
            description = "Search users by email and/or name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'REGIONAL_LEADS')")
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
    // ADMIN or COORDINATOR can view educators
    @Operation(summary = "Get all educators")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'COORDINATOR')")
    @GetMapping("/educators")
    public ResponseEntity<Page<UserListResponseDto>> getAllEducators(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllEducators(pageable));
    }

    // GET    /api/users/students - Get all students
    // ADMIN or EDUCATOR can view students
    @Operation(summary = "Get all students")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ADMIN', 'EDUCATOR')")
    @GetMapping("/students")
    public ResponseEntity<Page<UserListResponseDto>> getAllStudents(Pageable pageable) {
        return ResponseEntity.ok(userService.findAllStudents(pageable));
    }

    // PATCH  /api/users/{id}/consent - Update user SMS consent
    // ADMIN or user themselves can update consent
    @Operation(summary = "Update user SMS consent")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @PatchMapping("/{id}/consent")
    public ResponseEntity<UserResponseDto> updateSmsConsent(
            @PathVariable UUID id,
            @RequestParam Boolean consent) {
        return ResponseEntity.ok(userService.updateSmsConsent(id, consent));
    }

    // GET    /api/users/me - Get current user
    // Any authenticated user can access their own info
    @PreAuthorize("permitAll()")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok(user);
    }

    // GET    /api/users/me/schools - Get schools assigned to current user
    // Access based on user type
    @PreAuthorize("permitAll()")
    @GetMapping("/schools")
    public ResponseEntity<List<School>> getUserSchools(@CurrentUser User user) {
        // You can directly access user properties
        if (user.getUserType() == UserType.COORDINATOR) {
            return ResponseEntity.ok(new ArrayList<>(user.getAssignedSchools()));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}