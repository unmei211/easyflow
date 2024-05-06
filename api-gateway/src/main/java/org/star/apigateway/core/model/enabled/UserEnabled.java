package org.star.apigateway.core.model.enabled;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.star.apigateway.core.model.user.UserAuth;

@Entity
@Table(name = "user_enabled")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserEnabled {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default true")
    private Boolean enabled;

    @OneToOne
    @JoinColumn(unique = true, nullable = false, name = "user_id")
    private UserAuth user;
}
