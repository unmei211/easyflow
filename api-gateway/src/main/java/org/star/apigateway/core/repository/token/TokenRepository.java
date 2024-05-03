package org.star.apigateway.core.repository.token;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.star.apigateway.core.model.token.RefreshToken;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByUserId(String userId);
    Boolean existsByToken(String token);
    @Transactional
    @Modifying
    @Query("UPDATE RefreshToken rt SET rt.token = :token WHERE rt.user.id = :userId")
    void updateRefreshToken(@Param("token") String token, @Param("userId") String userId);
}
