package wi.roger.rogerWI.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wi.roger.rogerWI.DTO.School.SchoolReferenceDto;
import wi.roger.rogerWI.DTO.School.SchoolRequestDto;
import wi.roger.rogerWI.DTO.School.SchoolResponseDto;
import wi.roger.rogerWI.model.School;

@Component
@RequiredArgsConstructor
public class SchoolMapper {

    public static SchoolResponseDto toResponse(School school) {
        if (school == null) return null;

        return SchoolResponseDto.builder()
                .id(school.getId())
                .name(school.getName())
                .gradeLevel(school.getGradeLevel())
                .accessTo(school.getAccessTo())
                .districtName(school.getDistrictName())
                .county(school.getCounty())
                .activityRegions(school.getActivityRegions())
                .counselorEmail(school.getCounselorEmail())
                .dashboardLink(school.getDashboardLink())
                .rogerEmail(school.getrogerEmail())
                .permissionEmail(school.getPermissionEmail())
                .notes(school.getNotes())
                .isM7Educator(school.getIsM7Educator())
                .createdDate(school.getCreatedDate())
                .updatedDate(school.getUpdatedDate())
                .build();
    }

    public static School toEntity(SchoolRequestDto request) {
        if (request == null) return null;

        School school = new School();
        updateEntity(school, request);
        return school;
    }

    public static void updateEntity(School school, SchoolRequestDto request) {
        if (school == null || request == null) return;

        school.setName(request.getName());
        school.setGradeLevel(request.getGradeLevel());
        school.setAccessTo(request.getAccessTo());
        school.setDistrictName(request.getDistrictName());
        school.setCounty(request.getCounty());
        school.setActivityRegions(request.getActivityRegions());
        school.setCounselorEmail(request.getCounselorEmail());
        school.setDashboardLink(request.getDashboardLink());
        school.setrogerEmail(request.getrogerEmail());
        school.setPermissionEmail(request.getPermissionEmail());
        school.setNotes(request.getNotes());
        school.setIsM7Educator(request.getIsM7Educator());
    }

    public static SchoolReferenceDto toReference(School school) {
        if (school == null) return null;

        return SchoolReferenceDto.builder()
                .id(school.getId())
                .name(school.getName())
                .build();
    }
}