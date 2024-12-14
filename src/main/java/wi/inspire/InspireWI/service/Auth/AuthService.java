// service/AuthService.java
package wi.roger.rogerWI.service.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wi.roger.rogerWI.DTO.Auth.AuthResponse;
import wi.roger.rogerWI.DTO.Auth.LoginRequest;
import wi.roger.rogerWI.DTO.Auth.RegisterRequest;
import wi.roger.rogerWI.DTO.Auth.RegistrationRequest;
import wi.roger.rogerWI.DTO.User.Auth.AuthUserDto;
import wi.roger.rogerWI.DTO.User.Auth.AuthResponseDto;
import wi.roger.rogerWI.DTO.User.UserResponseDto;
import wi.roger.rogerWI.SecurityConfiguration.JwtTokenProvider;
import wi.roger.rogerWI.mapper.UserMapper;
import wi.roger.rogerWI.model.RefreshToken;
import wi.roger.rogerWI.model.School;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.repository.RefreshTokenRepository;
import wi.roger.rogerWI.repository.SchoolRepository;
import wi.roger.rogerWI.repository.UserRepository;
import wi.roger.rogerWI.service.ResourceExceptions.*;
import wi.roger.rogerWI.types.CommonEnums.*;


import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final SchoolRepository schoolRepository;

    @Transactional
    public UserResponseDto register(RegistrationRequest request) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        // Prevent ADMIN registration through public endpoint
        if (UserType.ADMIN.equals(request.getUserType())) {
            throw new InvalidRoleException("Cannot register as admin");
        }

        // Use mapper to create user entity
        User user = userMapper.toEntityForRegistration(request);

        // Handle school relationship if provided
        if (request.getSchoolId() != null) {
            School school = schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("School not found"));
            user.setSchool(school);
        }

        // Handle coordinator's assigned schools if any
        if (UserType.COORDINATOR.equals(request.getUserType()) &&
                request.getAssignedSchoolIds() != null &&
                !request.getAssignedSchoolIds().isEmpty()) {
            Set<School> assignedSchools = new HashSet<>(schoolRepository.findAllById(request.getAssignedSchoolIds()));
            user.setAssignedSchools(assignedSchools);
        }

        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }



    public AuthResponseDto login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        String accessToken = tokenProvider.generateToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(null)
                .user(convertToAuthUserDto(user))
                .build();
    }

    public AuthResponseDto register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserType(request.getUserType());
        user.setPhoneNumber(request.getPhoneNumber());

        if (request.getSchoolId() != null) {
            user.setSchool(schoolRepository.findById(request.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("School not found")));
        }

        user.setGradeLevel(request.getGradeLevel());
        user = userRepository.save(user);

        String accessToken = tokenProvider.generateToken(user);

        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(null)
                .user(convertToAuthUserDto(user))
                .build();
    }

    // Helper method to convert User to AuthUserDto
    private AuthUserDto convertToAuthUserDto(User user) {
        return AuthUserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .build();
    }

    private RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(LocalDateTime.now().plusDays(30));

        return refreshTokenRepository.save(refreshToken);
    }

    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        refreshTokenRepository.delete(token);
    }
}