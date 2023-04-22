package com.allang.authorizationserver.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
@Slf4j
public class KeyGenerator {
    public String[] generateRandomPairOfKeys() {
        SecureRandom random = new SecureRandom();
        byte[] clientIdBytes = new byte[10];
        random.nextBytes(clientIdBytes);
        String clientId = Base64.getUrlEncoder().encodeToString(clientIdBytes);
        byte[] clientSecretBytes = new byte[64];
        random.nextBytes(clientSecretBytes);
        log.info("Client ID: " + clientId);
        String clientSecret = Base64.getUrlEncoder().encodeToString(clientSecretBytes);
        log.info("Client Secret: " + clientSecret);
        return new String[]{clientId.substring(0,clientId.indexOf("=")),
                clientSecret.substring(0, clientSecret.indexOf("="))};
    }
}
