package org.star.apigateway.core.entity.password;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
//import org.apache.catalina.User;
import org.star.apigateway.core.entity.user.UserAuth;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_password")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, name = "password")
    private String value;

    @OneToOne
    @JoinColumn(unique = true, nullable = false, name = "user_id")
    private UserAuth user;

    public Password(final String hashPassword, final UserAuth user) {
        this.user = user;
        this.value = hashPassword;
    }
}
