package org.star.apigateway.core.entity.token;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "refresh_tokens")
public class RefreshToken implements Persistable<String> {
    @Column
    @NotNull
    private String userId;

    @Id
    @Column("refresh_token")
    @NotNull
    private String refreshToken;

    @Transient
    @Getter(AccessLevel.NONE)
    private Boolean isNew = false;

    @Override
    public String getId() {
        return refreshToken;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
