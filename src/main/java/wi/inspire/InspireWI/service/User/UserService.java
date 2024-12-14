package wi.roger.rogerWI.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.User.UserDto;
import wi.roger.rogerWI.DTO.User.UserListResponseDto;
import wi.roger.rogerWI.DTO.User.UserRequestDto;
import wi.roger.rogerWI.DTO.User.UserResponseDto;
import wi.roger.rogerWI.mapper.UserMapper;
import wi.roger.rogerWI.model.School;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.repository.SchoolRepository;
import wi.roger.rogerWI.repository.UserRepository;
import wi.roger.rogerWI.service.ResourceExceptions.*;
import wi.roger.rogerWI.types.CommonEnums.UserType;

import javax.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;


    @Transactional(readOnly = true)
    public Page<UserListResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.toResponse(user);
    }

    @Transactional
    public UserResponseDto create(UserRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = userMapper.toEntity(requestDto);  // Instance method

        if (requestDto.getSchoolId() != null) {
            School school = schoolRepository.findById(requestDto.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("School not found"));
            user.setSchool(school);
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto update(UUID id, UserRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (!user.getEmail().equals(requestDto.getEmail()) &&
                userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        userMapper.updateEntity(user, requestDto);

        if (requestDto.getSchoolId() != null) {
            School school = schoolRepository.findById(requestDto.getSchoolId())
                    .orElseThrow(() -> new ResourceNotFoundException("School not found"));
            user.setSchool(school);
        }

        return UserMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<UserListResponseDto> searchUsers(String email, String name, Pageable pageable) {
        return userRepository.findByEmailContainingOrNameContainingAllIgnoreCase(
                        email != null ? email : "",
                        name != null ? name : "",
                        pageable)
                .map(userMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserListResponseDto> findAllEducators(Pageable pageable) {
        return userRepository.findByUserType(UserType.EDUCATOR, pageable)
                .map(userMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserListResponseDto> findAllStudents(Pageable pageable) {
        return userRepository.findByUserType(UserType.STUDENT, pageable)
                .map(userMapper::toListResponse);
    }

    @Transactional
    public UserResponseDto updateSmsConsent(UUID id, Boolean consent) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setSmsConsent(consent);
        return UserMapper.toResponse(userRepository.save(user));
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We use the username parameter as email since we're using email for login
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getUserType().name())
                .build();
    }

    public UserDto getCurrentUser(UUID userId) {
        return convertToDTO(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
    }

    public Page<UserDto> getAllUsers(String email, String name, Set<UserType> userTypes, Pageable pageable) {
        Page<User> users = userRepository.findByFilters(email, name, userTypes, pageable);
        return users.map(this::convertToDTO);
    }

    @Transactional
    public void updatePassword(UUID userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private UserDto convertToDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setUserType(user.getUserType());
        dto.setDeployment(user.getDeployment());
        dto.setTitle(user.getTitle());
        dto.setServingCounties(user.getServingCounties());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setWorkPhone(user.getWorkPhone());
        dto.setPhoneType(user.getPhoneType());
        dto.setSmsConsent(user.getSmsConsent());
        dto.setParentGuardianEmail(user.getParentGuardianEmail());
        dto.setSchoolId(user.getSchool() != null ? user.getSchool().getId() : null);
        dto.setStudentId(user.getStudentId());
        dto.setIntendedMajor(user.getIntendedMajor());
        dto.setGradeLevel(user.getGradeLevel());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setEducatorRole(user.getEducatorRole());
        dto.setCustomRole(user.getCustomRole());
        return dto;
    }
}