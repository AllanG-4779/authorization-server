package com.allang.authorizationserver.controller;

import com.allang.authorizationserver.dto.AccessToken;
import com.allang.authorizationserver.dto.CreateUserRequest;
import com.allang.authorizationserver.service.imp.AuthenticationServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register-client")
    public ResponseEntity<?> registerClient(@RequestBody CreateUserRequest createUserRequest) {
        AccessToken response = authenticationService.registerClient(createUserRequest);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }
}


