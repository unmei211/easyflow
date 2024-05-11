package org.star.socialservice.core.services.social;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.star.socialservice.core.repository.social.SocialRepository;

@SpringBootTest
public class SocialServiceTest {
    @Autowired
    private SocialService socialService;
    private SocialRepository socialRepository;



}
