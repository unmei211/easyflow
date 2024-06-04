package org.star.apigateway.core.entity.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.aot.generate.Generated;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.star.apigateway.core.entity.password.Password;
import org.star.apigateway.core.entity.roles.Role;
import org.star.apigateway.core.entity.token.RefreshToken;

import java.util.*;

@Table(name = "users_auth")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserAuth implements Persistable<String> {
    @Id
    @Column("id")
    private String id;

    @Transient
    private boolean isNew = false;

    @Override
    @Transient
    public boolean isNew() {
        return isNew;
    }

    @Column
//    @NotNull
    private Boolean enabled = true;

//    public UserAuth(final String id) {
//        this.enabled = true;
//        this.id = "1";
//    }
}
