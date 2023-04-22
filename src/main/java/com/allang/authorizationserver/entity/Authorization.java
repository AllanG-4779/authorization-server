package com.allang.authorizationserver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`authorization`")
public class Authorization {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String registeredClientId;
    private String principalName;
    private String authorizationGrantType;
   @Lob // For texts longer than 255 characters
    private String authorizedScopes;
   @Lob
    private String attributes;
   @Lob
    private String state;
 @Lob
    private String authorizationCodeValue;
    private Instant authorizationCodeIssuedAt;
    private Instant authorizationCodeExpiresAt;
    private String authorizationCodeMetadata;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String accessTokenValue;
    private Instant accessTokenIssuedAt;
    private Instant accessTokenExpiresAt;
   @Column(length = 4000) // For texts longer than 255 characters
    private String accessTokenMetadata;
    private String accessTokenType;
    @Lob
    private String accessTokenScopes;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String refreshTokenValue;
    private Instant refreshTokenIssuedAt;
    private Instant refreshTokenExpiresAt;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String refreshTokenMetadata;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private String oidcIdTokenValue;
    private Instant oidcIdTokenIssuedAt;
    private Instant oidcIdTokenExpiresAt;
    @Column(length = 2000)

    private String oidcIdTokenMetadata;
    @Column(length = 2000)
    private String oidcIdTokenClaims;

}

