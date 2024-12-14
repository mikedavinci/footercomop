// repository/RefreshTokenRepository.java
package wi.roger.rogerWI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wi.roger.rogerWI.model.RefreshToken;
import wi.roger.rogerWI.model.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user);
}