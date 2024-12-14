package wi.roger.rogerWI.service.Search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import wi.roger.rogerWI.DTO.Activity.ActivityListDto;
import wi.roger.rogerWI.DTO.Company.CompanyListDto;
import wi.roger.rogerWI.DTO.User.UserListResponseDto;
import wi.roger.rogerWI.mapper.ActivityMapper;
import wi.roger.rogerWI.mapper.CompanyMapper;
import wi.roger.rogerWI.mapper.UserMapper;
import wi.roger.rogerWI.repository.ActivityRepository;
import wi.roger.rogerWI.repository.CompanyRepository;
import wi.roger.rogerWI.repository.UserRepository;
import wi.roger.rogerWI.types.CommonEnums.*;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final ActivityRepository activityRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
                .map(userMapper::toListResponse);
    }
}