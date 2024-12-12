package wi.inspire.InspireWI.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wi.inspire.InspireWI.DTO.Career.CareerPathwayRequestDto;
import wi.inspire.InspireWI.DTO.Career.CareerPathwayResponseDto;
import wi.inspire.InspireWI.DTO.Career.CareerReferenceDto;
import wi.inspire.InspireWI.model.CareerPathway;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CareerPathwayMapper {

    public static CareerPathwayResponseDto toResponse(CareerPathway careerPathway) {
        if (careerPathway == null) return null;

        return CareerPathwayResponseDto.builder()
                .id(careerPathway.getId())
                .name(careerPathway.getName())
                .cluster(careerPathway.getCluster())
                .careers(careerPathway.getCareers().stream()
                        .map(career -> CareerReferenceDto.builder()
                                .id(career.getId())
                                .name(career.getName())
                                .build())
                        .collect(Collectors.toSet()))
                .createdDate(careerPathway.getCreatedDate())
                .updatedDate(careerPathway.getUpdatedDate())
                .build();
    }

    public static CareerPathway toEntity(CareerPathwayRequestDto request) {
        if (request == null) return null;

        CareerPathway careerPathway = new CareerPathway();
        updateEntity(careerPathway, request);
        return careerPathway;
    }

    public static void updateEntity(CareerPathway careerPathway, CareerPathwayRequestDto request) {
        if (careerPathway == null || request == null) return;

        careerPathway.setName(request.getName());
        careerPathway.setCluster(request.getCluster());
        // Note: Career assignments should be handled in service layer
    }

    public static CareerReferenceDto toReference(CareerPathway careerPathway) {
        if (careerPathway == null) return null;

        return CareerReferenceDto.builder()
                .id(careerPathway.getId())
                .name(careerPathway.getName())
                .build();
    }
}