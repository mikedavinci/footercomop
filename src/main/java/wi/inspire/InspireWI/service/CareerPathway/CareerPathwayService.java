package wi.inspire.InspireWI.service.CareerPathway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wi.inspire.InspireWI.DTO.Career.CareerPathwayRequestDto;
import wi.inspire.InspireWI.DTO.Career.CareerPathwayResponseDto;
import wi.inspire.InspireWI.mapper.CareerPathwayMapper;
import wi.inspire.InspireWI.service.ResourceExceptions.*;

import wi.inspire.InspireWI.model.*;
import wi.inspire.InspireWI.repository.*;
import wi.inspire.InspireWI.types.CommonEnums;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CareerPathwayService {
    private final CareerPathwayRepository careerPathwayRepository;
    private final CareerRepository careerRepository;

    @Transactional(readOnly = true)
    public Page<CareerPathwayResponseDto> findAll(Pageable pageable) {
        return careerPathwayRepository.findAll(pageable)
                .map(CareerPathwayMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public CareerPathwayResponseDto findById(UUID id) {
        CareerPathway pathway = careerPathwayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career pathway not found with id: " + id));
        return CareerPathwayMapper.toResponse(pathway);    }

    @Transactional
    public CareerPathwayResponseDto create(CareerPathwayRequestDto requestDto) {
        if (careerPathwayRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("Career pathway name already exists");
        }

        CareerPathway pathway = CareerPathwayMapper.toEntity(requestDto);

        if (requestDto.getCareerIds() != null && !requestDto.getCareerIds().isEmpty()) {
            Set<Career> careers = requestDto.getCareerIds().stream()
                    .map(careerId -> careerRepository.findById(careerId)
                            .orElseThrow(() -> new ResourceNotFoundException("Career not found with id: " + careerId)))
                    .collect(Collectors.toSet());
            pathway.setCareers(careers);
        }

        return CareerPathwayMapper.toResponse(careerPathwayRepository.save(pathway));
    }

    @Transactional
    public CareerPathwayResponseDto update(UUID id, CareerPathwayRequestDto requestDto) {
        CareerPathway pathway = careerPathwayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Career pathway not found"));

        if (!pathway.getName().equalsIgnoreCase(requestDto.getName()) &&
                careerPathwayRepository.existsByNameIgnoreCase(requestDto.getName())) {
            throw new IllegalArgumentException("Career pathway name already exists");
        }

        CareerPathwayMapper.updateEntity(pathway, requestDto);

        if (requestDto.getCareerIds() != null && !requestDto.getCareerIds().isEmpty()) {
            Set<Career> careers = requestDto.getCareerIds().stream()
                    .map(careerId -> careerRepository.findById(careerId)
                            .orElseThrow(() -> new ResourceNotFoundException("Career not found with id: " + careerId)))
                    .collect(Collectors.toSet());
            pathway.setCareers(careers);
        }

        return CareerPathwayMapper.toResponse(careerPathwayRepository.save(pathway));
    }

    @Transactional
    public void delete(UUID id) {
        if (!careerPathwayRepository.existsById(id)) {
            throw new ResourceNotFoundException("Career pathway not found with id: " + id);
        }
        careerPathwayRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CareerPathwayResponseDto> findByCluster(CommonEnums.Cluster cluster) {
        return careerPathwayRepository.findByCluster(cluster).stream()
                .map(CareerPathwayMapper::toResponse)
                .collect(Collectors.toList());
    }
}
