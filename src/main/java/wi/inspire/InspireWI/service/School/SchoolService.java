// School Controller Endpoints
// GET    /api/schools                - Get all schools (paginated)
// GET    /api/schools/{id}           - Get school by ID
// POST   /api/schools                - Create new school
// PUT    /api/schools/{id}           - Update existing school
// DELETE /api/schools/{id}           - Delete school
// GET    /api/schools/search         - Search schools by grade level/county/access type

package wi.roger.rogerWI.service.School;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.roger.rogerWI.DTO.School.*;
import wi.roger.rogerWI.mapper.SchoolMapper;
import wi.roger.rogerWI.service.ResourceExceptions.*;
import wi.roger.rogerWI.model.School;
import wi.roger.rogerWI.repository.SchoolRepository;
import wi.roger.rogerWI.types.CommonEnums.*;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    @Transactional(readOnly = true)
    public Page<SchoolResponseDto> findAll(Pageable pageable) {
        return schoolRepository.findAll(pageable)
                .map(SchoolMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public SchoolResponseDto findById(UUID id) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        return SchoolMapper.toResponse(school);
    }

    @Transactional
    public SchoolResponseDto create(SchoolRequestDto requestDto) {
        if (schoolRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("School name already exists");
        }

        School school = SchoolMapper.toEntity(requestDto);
        return SchoolMapper.toResponse(schoolRepository.save(school));
    }

    @Transactional
    public SchoolResponseDto update(UUID id, SchoolRequestDto requestDto) {
        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));

        if (!school.getName().equalsIgnoreCase(requestDto.getName()) &&
                schoolRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("School name already exists");
        }

        SchoolMapper.updateEntity(school, requestDto);
        return SchoolMapper.toResponse(schoolRepository.save(school));
    }

    @Transactional
    public void delete(UUID id) {
        if (!schoolRepository.existsById(id)) {
            throw new ResourceNotFoundException("School not found with id: " + id);
        }
        schoolRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Page<SchoolResponseDto> searchSchools(
            GradeLevel gradeLevel,
            County county,
            AccessTo accessTo,
            Pageable pageable) {
        return schoolRepository.findByFilters(gradeLevel, county, accessTo, pageable)
                .map(SchoolMapper::toResponse);
    }
}