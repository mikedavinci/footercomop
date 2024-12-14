package wi.roger.rogerWI.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import wi.roger.rogerWI.model.User;
import wi.roger.rogerWI.types.CommonEnums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Authentication related methods
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE users u SET u.password = :newPassword WHERE u.id = :userId")
    void updatePassword(@Param("userId") UUID userId, @Param("newPassword") String newPassword);

    // Search and filter methods
    @Query("SELECT u FROM users u WHERE " +
            "LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%')) OR " +
            "LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<User> findByEmailContainingOrNameContainingAllIgnoreCase(
            @Param("email") String email,
            @Param("name") String name,
            Pageable pageable);

    Page<User> findByUserType(UserType userType, Pageable pageable);

    @Query("SELECT u FROM users u WHERE " +
            "(:email is null OR LOWER(u.email) LIKE LOWER(CONCAT('%', :email, '%'))) " +
            "AND (:name is null OR LOWER(u.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (COALESCE(:userTypes, null) is null OR u.userType IN :userTypes)")
    Page<User> findByFilters(
            @Param("email") String email,
            @Param("name") String name,
            @Param("userTypes") Set<UserType> userTypes,
            Pageable pageable
    );

    // School-related queries
    @Query("SELECT u FROM users u WHERE u.school.id = :schoolId")
    Page<User> findBySchoolId(@Param("schoolId") UUID schoolId, Pageable pageable);

    @Query("SELECT DISTINCT u FROM users u " +
            "LEFT JOIN FETCH u.assignedSchools " +
            "WHERE u.id = :userId")
    Optional<User> findByIdWithSchools(@Param("userId") UUID userId);

    @Query("SELECT COUNT(u) FROM users u WHERE u.school.id = :schoolId")
    long countBySchoolId(@Param("schoolId") UUID schoolId);
}