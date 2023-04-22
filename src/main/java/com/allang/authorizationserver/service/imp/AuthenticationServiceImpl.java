package com.allang.authorizationserver.service.imp;

import com.allang.authorizationserver.config.KeyGenerator;
import com.allang.authorizationserver.dto.CreateUserRequest;
import com.allang.authorizationserver.entity.AppUser;
import com.allang.authorizationserver.entity.Client;
import com.allang.authorizationserver.repository.AppUserRepository;
import com.allang.authorizationserver.repository.ClientRepository;
import com.allang.authorizationserver.repository.JpaRegisteredClientRepository;
import com.allang.authorizationserver.service.AuthenticationService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeyGenerator keyGenerator;

    private final JpaRegisteredClientRepository registeredClientRepository;

    public AuthenticationServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, KeyGenerator keyGenerator, ClientRepository clientRepository, JpaRegisteredClientRepository registeredClientRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.keyGenerator = keyGenerator;

        this.registeredClientRepository = registeredClientRepository;
    }

    @Override
    public Object registerUser(CreateUserRequest request) {
        AppUser appUser = AppUser.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .build();
        appUserRepository.save(appUser);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", request.getUsername());
        jsonObject.addProperty("email", request.getEmail());
        jsonObject.addProperty("createdAt", String.valueOf(LocalDateTime.now()));
        return jsonObject;
    }

    @Override
    public Object registerClient(String clientNameId){
        Optional<AppUser> user = appUserRepository.findById(clientNameId);
        if (user.isEmpty()){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("error", "User not found");
            return jsonObject;
        }
       String[] clientCredentials = keyGenerator.generateRandomPairOfKeys();
        Client newClient = Client.builder()
                .id(UUID.randomUUID().toString())
                .clientId(clientCredentials[0])
                .clientSecret(passwordEncoder.encode(clientCredentials[1]))
                .clientName("Allang")
                .clientIdIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.of("+3")))
                .clientSecretExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("+3")))
                .clientAuthenticationMethods(ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue())
                .authorizationGrantTypes(AuthorizationGrantType.CLIENT_CREDENTIALS.getValue())
                .scopes("message_read,message_write,"+ OidcScopes.PROFILE)
                .redirectUris("http://127.0.0.1:8080/authorized")
                .appUser(user.get())
                .build();
        RegisteredClient registeredClient = registeredClientRepository.toObject(newClient);

        registeredClientRepository.save(registeredClient);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("clientId", clientCredentials[0]);
        jsonObject.addProperty("clientSecret", clientCredentials[1]);
        return jsonObject;
    }

}
