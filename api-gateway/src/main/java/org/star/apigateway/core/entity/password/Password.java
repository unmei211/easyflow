package org.star.apigateway.core.entity.password;


import jakarta.validation.constraints.NotNull;
import lombok.*;
//import org.apache.catalina.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.star.apigateway.core.entity.user.UserAuth;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "user_password")
public class Password implements Persistable<String> {
    @Column
    @NotNull
    private String password;

    @Id
    @NotNull
    private String userId;

    public Password(final String hashPassword, final UserAuth user) {
//        this.userId = user.getId();
        this.password = hashPassword;
    }

    @Override
    public String getId() {
        return userId;
    }

    @Transient
    private Boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }
}
