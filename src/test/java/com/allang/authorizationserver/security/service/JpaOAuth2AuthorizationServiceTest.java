package com.allang.authorizationserver.security.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JpaOAuth2AuthorizationServiceTest {
    @Autowired JpaOAuth2AuthorizationService service;
    @Test
    void checkJson() {
        String json = "{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"metadata.token.claims\":{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"sub\":\"N5pFYQDp25xtyw\",\"aud\":[\"java.util.Collections$SingletonList\",[\"N5pFYQDp25xtyw\"]],\"nbf\":[\"java.time.Instant\",1682772376.480119019],\"user_id\":\"Allan Onyango\",\"iss\":[\"java.net.URL\",\"http://localhost:8083\"],\"exp\":[\"java.time.Instant\",1682772676.480119019],\"iat\":[\"java.time.Instant\",1682772376.480119019],\"authorities\":\"ADMIN_USER\"},\"metadata.token.invalidated\":false}";
                System.out.println(service.parseMap(json));
    }

}