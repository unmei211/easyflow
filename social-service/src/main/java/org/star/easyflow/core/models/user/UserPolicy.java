package org.star.easyflow.core.models.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.easyflow.core.models.policy.PolicyBundle;

@Entity
@Table(name = "user_policy")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "allowed_to_friend_request", nullable = false)
    private Boolean allowedToFriendRequest;

    @Column(name = "allowed_send_task", nullable = false)
    private Boolean allowedSendTask;

    public PolicyBundle getPolicyBundle() {
        return new PolicyBundle(this.getAllowedToFriendRequest(), this.getAllowedSendTask());
    }

    public UserPolicy(final String userId) {
        this.allowedSendTask = true;
        this.allowedToFriendRequest = true;
        User user = new User();
        user.setId(userId);
        this.user = user;
    }
}
