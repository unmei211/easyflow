package org.star.socialservice.core.entity.user;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import org.star.socialservice.core.entity.contract.Contract;
import org.star.socialservice.core.entity.policies.UserPolicies;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "social_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SocialUser {
    @Id
    private String id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "friend",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    )
    private Set<SocialUser> friends = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "friend_request",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "sender_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "sender_id"})
    )
    private Set<SocialUser> friendRequests = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserPolicies policy;

    @OneToMany(mappedBy = "assigmenter", cascade = CascadeType.REMOVE)
    private List<Contract> ownContracts;

    @OneToMany(mappedBy = "holder", cascade = CascadeType.REMOVE)
    private List<Contract> signedContracts;

    public SocialUser(String userId) {
        this.id = userId;
    }

    public void removeFriendRequest(SocialUser toDelete) {
        getFriendRequests().remove(toDelete);
    }

    public void removeFriend(SocialUser friendToDelete) {
        getFriends().remove(friendToDelete);
        friendToDelete.getFriends().remove(this);
    }

    public SocialUser setPolicy(final UserPolicies policy) {
        this.policy = policy;
        policy.setUser(this);
        return this;
    }

    public SocialUser addFriend(final SocialUser socialUser) {
        if (this.getId().equals(socialUser.getId())) {
            return this;
        }

        if (!this.getFriends().contains(socialUser)) {
            this.getFriends().add(socialUser);
        }
        if (!socialUser.getFriends().contains(this)) {
            socialUser.getFriends().add(this);
        }
        return this;
    }

    public SocialUser addFriendRequest(final SocialUser socialUser) {
        if (!this.getFriendRequests().contains(socialUser)) {
            this.getFriendRequests().add(socialUser);
        }
        return this;
    }
}
