package org.star.easyflow.core.models.user;

import jakarta.persistence.*;
import lombok.*;
import org.star.easyflow.core.models.contract.Contract;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String login;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany
    @JoinTable(
            name = "friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    )
    private Set<User> friends = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private UserPolicy policy;

    @OneToMany(mappedBy = "assigmenter", cascade = CascadeType.REMOVE)
    private List<Contract> ownContracts;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.REMOVE)
    private List<Contract> signedContracts;

    public User(
            final String login,
            final String email
    ) {
        this.email = email;
        this.login = login;
    }
}
