package org.star.apigateway.core.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.apigateway.core.model.roles.Role;
import org.star.apigateway.core.model.token.RefreshToken;

import java.util.*;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean enabled;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
    )
    private List<Role> roles = new ArrayList<>();

    public List<String> getPresentRoles() {
        List<String> presentRoles = new ArrayList<>();
        List<Role> userRoles = this.getRoles();
        for (Role role : userRoles) {
            presentRoles.add(role.getRole());
        }
        return presentRoles;
    }

    public User(
            final String login,
            final String email,
            final String password
    ) {
        this.email = email;
        this.login = login;
        this.password = password;
        this.enabled = true;
    }
}
