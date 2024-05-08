package org.star.apigateway.core.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.apigateway.core.model.enabled.UserEnabled;
import org.star.apigateway.core.model.password.Password;
import org.star.apigateway.core.model.roles.Role;
import org.star.apigateway.core.model.token.RefreshToken;

import java.util.*;

@Table(name = "users")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserAuth {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Password password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private RefreshToken refreshToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserEnabled enabled;

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

    public UserAuth(
            final String id
    ) {
        this.id = id;
    }

    public Boolean isEnabled() {
        System.out.println(getEnabled() + " bebebe");
        return getEnabled().getEnabled();
    }
}
