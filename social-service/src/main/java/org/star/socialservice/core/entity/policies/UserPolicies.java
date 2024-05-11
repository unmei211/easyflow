package org.star.socialservice.core.entity.policies;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.star.socialservice.core.entity.user.SocialUser;
import org.star.socialservice.core.model.policies.PolicyBundle;

@Entity
@Table(name = "user_policies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPolicies {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private SocialUser user;

    @Column(name = "allowed_to_friend_request", nullable = false)
    private Boolean allowedToFriendRequest;

    @Column(name = "allowed_send_task", nullable = false)
    private Boolean allowedSendTask;

    public PolicyBundle getPolicyBundle() {
        return new PolicyBundle(this.getAllowedToFriendRequest(), this.getAllowedSendTask());
    }

    public UserPolicies(final String userId) {
        this.allowedSendTask = true;
        this.allowedToFriendRequest = true;
        SocialUser user = new SocialUser();
        user.setId(userId);
        this.user = user;
    }
}
