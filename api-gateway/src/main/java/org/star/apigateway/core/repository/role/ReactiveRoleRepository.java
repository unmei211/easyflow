package org.star.apigateway.core.repository.role;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.star.apigateway.core.entity.roles.Role;
//
//import java.util.Optional;
//
//public interface RoleRepository extends JpaRepository<Role, String> {
//    Optional<Role> findByRole(String role);
//}

import org.springframework.data.annotation.Transient;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.star.apigateway.core.entity.roles.Role;
import org.star.apigateway.microservice.share.model.user.UserViaId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveRoleRepository extends ReactiveCrudRepository<Role, String> {

    @Modifying
    @Transient
    @Query("INSERT INTO user_role(user_id, role_id) VALUES(:userId, :role)")
    Mono<Void> bindNewUserWith(@Param("userId") String userId, @Param("role") String role);

    @Query("SELECT role.role FROM role JOIN user_role on role.role = user_role.role_id WHERE user_id = :userId")
    Flux<Role> findUserRoles(final @Param("userId") String userId);
}