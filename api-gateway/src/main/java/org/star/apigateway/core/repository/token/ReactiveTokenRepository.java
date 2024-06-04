package org.star.apigateway.core.repository.token;


import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.star.apigateway.core.entity.token.RefreshToken;
import reactor.core.publisher.Mono;

//@Repository
//public interface TokenRepository extends JpaRepository<RefreshToken, String> {
//    Optional<RefreshToken> findByUserId(String userId);
//    Boolean existsByToken(String token);
//    @Transactional
//    @Modifying
//    @Query("UPDATE RefreshToken rt SET rt.token = :token WHERE rt.user.id = :userId")
//    void updateRefreshToken(@Param("token") String token, @Param("userId") String userId);
//}

public interface ReactiveTokenRepository extends ReactiveCrudRepository<RefreshToken, String> {
    Mono<RefreshToken> findByUserId(String userId);

    @Transactional
    @Modifying
    @Query("UPDATE refresh_tokens SET refresh_token = :token WHERE user_id = :userId")
    Mono<Void> updateRefreshToken(@Param("token") String token, @Param("userId") String userId);

    Mono<Boolean> existsByUserId(String userId);
}
