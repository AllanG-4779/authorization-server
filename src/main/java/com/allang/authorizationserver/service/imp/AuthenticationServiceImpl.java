package com.allang.authorizationserver.service.imp;

import com.allang.authorizationserver.config.KeyGenerator;
import com.allang.authorizationserver.dto.AccessToken;
import com.allang.authorizationserver.dto.CreateUserRequest;
import com.allang.authorizationserver.entity.AppUser;
import com.allang.authorizationserver.entity.Client;
import com.allang.authorizationserver.exceptions.exception.ParameterExists;
import com.allang.authorizationserver.repository.AppUserRepository;
import com.allang.authorizationserver.repository.ClientRepository;
import com.allang.authorizationserver.repository.JpaRegisteredClientRepository;
import com.allang.authorizationserver.service.AuthenticationService;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

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
    public AccessToken registerClient(CreateUserRequest userRequest){
       String[] clientCredentials = keyGenerator.generateRandomPairOfKeys();
        Client newClient = Client.builder()
                .id(UUID.randomUUID().toString())
                .clientId(clientCredentials[0])
                .clientSecret(passwordEncoder.encode(clientCredentials[1]))
                .clientName(userRequest.getUsername())
                .clientIdIssuedAt(LocalDateTime.now().toInstant(ZoneOffset.of("+3")))
                .clientSecretExpiresAt(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of("+3")))
                .clientAuthenticationMethods(""+ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue()+","+ ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue())
                .authorizationGrantTypes(""+AuthorizationGrantType.CLIENT_CREDENTIALS.getValue()+","+AuthorizationGrantType.REFRESH_TOKEN)
                .scopes("message_read,message_write,openid")
                .redirectUris("http://127.0.0.1:8080/authorized")
                .build();
        RegisteredClient registeredClient = registeredClientRepository.toObject(newClient);

        if (createUser(userRequest)){
            registeredClientRepository.save(registeredClient);
            return AccessToken.builder()
                    .clientId(clientCredentials[0])
                    .clientSecret(clientCredentials[1])
                    .build();
        }else{
            throw new IllegalArgumentException("User creation failed");
        }


    }

    public boolean createUser(CreateUserRequest request){
//        Check if the user exists
        Optional<AppUser> userUsername = appUserRepository.findByUsername(request.getUsername());
        Optional<AppUser> userEmail = appUserRepository.findAppUserByEmail(request.getEmail());
        if (userUsername.isPresent()){
            throw new ParameterExists("Username already exists");
        }
        if ( userEmail.isPresent()){
            throw  new ParameterExists("Email already exists");
        }

        AppUser userRequest = AppUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .roles("USER,PARENT")
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
       AppUser appUser =  appUserRepository.save(userRequest);
      return !appUser.getUsername().isEmpty();
    }

}
