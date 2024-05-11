package org.star.socialservice.core.repository.social;

import org.springframework.data.jpa.repository.JpaRepository;
import org.star.socialservice.core.entity.user.SocialUser;

public interface SocialRepository extends JpaRepository<SocialUser, String> {

}
