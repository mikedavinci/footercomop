package wi.roger.rogerWI.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wi.roger.rogerWI.DTO.School.SchoolReferenceDto;
import wi.roger.rogerWI.DTO.User.*;
import wi.roger.rogerWI.model.School;
import wi.roger.rogerWI.model.User;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public static UserResponseDto toResponse(User user) {
        if (user == null) return null;

        switch (user.getUserType()) {
            case COORDINATOR:
                return toCoordinatorResponse(user);
            case REGIONAL_LEADS:
                return toRegionalLeadResponse(user);
            case STUDENT:
                return toStudentResponse(user);
            case EDUCATOR:
                return toEducatorResponse(user);
            default:
                return toBaseUserResponse(user);
        }
    }

    private static UserResponseDto toBaseUserResponse(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .phoneNumber(user.getPhoneNumber())
                .workPhone(user.getWorkPhone())
                .phoneType(user.getPhoneType())
                .smsConsent(user.getSmsConsent())
                .parentGuardianEmail(user.getParentGuardianEmail())
                .school(toSchoolReference(user.getSchool()))
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }

    private static CoordinatorResponseDto toCoordinatorResponse(User user) {
        return CoordinatorResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .phoneNumber(user.getPhoneNumber())
                .workPhone(user.getWorkPhone())
                .phoneType(user.getPhoneType())
                .smsConsent(user.getSmsConsent())
                .school(toSchoolReference(user.getSchool()))
                .servingCounties(user.getServingCounties())
                .assignedSchools(user.getAssignedSchools().stream()
                        .map(UserMapper::toSchoolReference)
                        .collect(Collectors.toSet()))
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }

    private static RegionalLeadResponseDto toRegionalLeadResponse(User user) {
        return RegionalLeadResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .phoneNumber(user.getPhoneNumber())
                .workPhone(user.getWorkPhone())
                .phoneType(user.getPhoneType())
                .smsConsent(user.getSmsConsent())
                .deployment(user.getDeployment())
                .title(user.getTitle())
                .servingCounties(user.getServingCounties())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }

    private static StudentResponseDto toStudentResponse(User user) {
        return StudentResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .phoneNumber(user.getPhoneNumber())
                .phoneType(user.getPhoneType())
                .smsConsent(user.getSmsConsent())
                .parentGuardianEmail(user.getParentGuardianEmail())
                .school(toSchoolReference(user.getSchool()))
                .studentId(user.getStudentId())
                .intendedMajor(user.getIntendedMajor())
                .gradeLevel(user.getGradeLevel())
                .dateOfBirth(user.getDateOfBirth())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }

    private static EducatorResponseDto toEducatorResponse(User user) {
        return EducatorResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .phoneNumber(user.getPhoneNumber())
                .workPhone(user.getWorkPhone())
                .phoneType(user.getPhoneType())
                .smsConsent(user.getSmsConsent())
                .school(toSchoolReference(user.getSchool()))
                .educatorRole(user.getEducatorRole())
                .customRole(user.getCustomRole())
                .gradeLevel(user.getGradeLevel())
                .createdDate(user.getCreatedDate())
                .updatedDate(user.getUpdatedDate())
                .build();
    }

    private static SchoolReferenceDto toSchoolReference(School school) {
        if (school == null) return null;
        return SchoolReferenceDto.builder()
                .id(school.getId())
                .name(school.getName())
                .build();
    }

    public static User toEntity(UserRequestDto request) {
        if (request == null) return null;

        User user = new User();
        updateEntity(user, request);
        return user;
    }

    public static void updateEntity(User user, UserRequestDto request) {
        if (user == null || request == null) return;

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setUserType(request.getUserType());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setWorkPhone(request.getWorkPhone());
        user.setPhoneType(request.getPhoneType());
        user.setSmsConsent(request.getSmsConsent());
        user.setParentGuardianEmail(request.getParentGuardianEmail());

        if (request instanceof CoordinatorRequestDto) {
            CoordinatorRequestDto coordRequest = (CoordinatorRequestDto) request;
            user.setServingCounties(coordRequest.getServingCounties());
            // Note: School assignments should be handled in service layer
        }
        else if (request instanceof RegionalLeadRequestDto) {
            RegionalLeadRequestDto leadRequest = (RegionalLeadRequestDto) request;
            user.setDeployment(leadRequest.getDeployment());
            user.setTitle(leadRequest.getTitle());
            user.setServingCounties(leadRequest.getServingCounties());
        }
        else if (request instanceof StudentRequestDto) {
            StudentRequestDto studentRequest = (StudentRequestDto) request;
            user.setStudentId(studentRequest.getStudentId());
            user.setIntendedMajor(studentRequest.getIntendedMajor());
            user.setGradeLevel(studentRequest.getGradeLevel());
            user.setDateOfBirth(studentRequest.getDateOfBirth());
        }
        else if (request instanceof EducatorRequestDto) {
            EducatorRequestDto educatorRequest = (EducatorRequestDto) request;
            user.setEducatorRole(educatorRequest.getEducatorRole());
            user.setCustomRole(educatorRequest.getCustomRole());
            user.setGradeLevel(educatorRequest.getGradeLevel());
        }
    }

    public static UserListResponseDto toListResponse(User user) {
        if (user == null) return null;

        return UserListResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .userType(user.getUserType())
                .school(toSchoolReference(user.getSchool()))
                .servingCounties(user.getServingCounties())
                .deployment(user.getDeployment())
                .title(user.getTitle())
                .educatorRole(user.getEducatorRole())
                .gradeLevel(user.getGradeLevel())
                .build();
    }
}