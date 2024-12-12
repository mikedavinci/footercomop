package wi.inspire.InspireWI.service.Search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wi.inspire.InspireWI.DTO.Activity.ActivityListDto;
import wi.inspire.InspireWI.DTO.Company.CompanyListDto;
import wi.inspire.InspireWI.DTO.User.UserListResponseDto;
import wi.inspire.InspireWI.mapper.ActivityMapper;
import wi.inspire.InspireWI.mapper.CompanyMapper;
import wi.inspire.InspireWI.mapper.UserMapper;
import wi.inspire.InspireWI.repository.ActivityRepository;
import wi.inspire.InspireWI.repository.CompanyRepository;
import wi.inspire.InspireWI.repository.UserRepository;
import wi.inspire.InspireWI.types.CommonEnums.*;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ActivityRepository activityRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public Page<ActivityListDto> searchActivities(
            String query,
            Set<ActivityCategory> categories,
            DeliveryMode deliveryMode,
            Boolean isPrescheduled,
            Pageable pageable) {

        return activityRepository.findByFilters(
                        query,
                        categories,
                        deliveryMode,
                        isPrescheduled,
                        pageable)
                .map(ActivityMapper::toListResponse);
    }

    public Page<CompanyListDto> searchCompanies(
            String name,
            Set<Cluster> clusters,
            Set<County> regions,
            TourType tourType,
            Pageable pageable) {

        return companyRepository.findByFilters(
                        name,
                        clusters,
                        regions,
                        tourType,
                        pageable)
                .map(CompanyMapper::toListResponse);
    }

    public Page<UserListResponseDto> searchUsers(
            String email,
            String name,
            Set<UserType> userTypes,
            Pageable pageable) {

        return userRepository.findByFilters(
                        email,
                        name,
                        userTypes,
                        pageable)
                .map(UserMapper::toListResponse);
    }
}