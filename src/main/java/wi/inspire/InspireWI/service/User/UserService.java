package wi.inspire.InspireWI.service.User;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.inspire.InspireWI.DTO.User.*;
import wi.inspire.InspireWI.service.ResourceExceptions.*;
import wi.inspire.InspireWI.mapper.UserMapper;
import wi.inspire.InspireWI.model.School;
import wi.inspire.InspireWI.model.User;
import wi.inspire.InspireWI.repository.SchoolRepository;
import wi.inspire.InspireWI.repository.UserRepository;
import wi.inspire.InspireWI.types.CommonEnums.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public Page<UserListResponseDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserMapper::toListResponse);
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

        User user = UserMapper.toEntity(requestDto);

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

        UserMapper.updateEntity(user, requestDto);

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
                .map(UserMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserListResponseDto> findAllEducators(Pageable pageable) {
        return userRepository.findByUserType(UserType.EDUCATOR, pageable)
                .map(UserMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<UserListResponseDto> findAllStudents(Pageable pageable) {
        return userRepository.findByUserType(UserType.STUDENT, pageable)
                .map(UserMapper::toListResponse);
    }

    @Transactional
    public UserResponseDto updateSmsConsent(UUID id, Boolean consent) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setSmsConsent(consent);
        return UserMapper.toResponse(userRepository.save(user));
    }
}