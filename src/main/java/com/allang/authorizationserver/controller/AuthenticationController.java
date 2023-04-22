package com.allang.authorizationserver.controller;

import com.allang.authorizationserver.dto.AccessToken;
import com.allang.authorizationserver.dto.CreateUserRequest;
import com.allang.authorizationserver.service.imp.AuthenticationServiceImpl;
import com.nimbusds.jose.shaded.gson.JsonObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {
    private final AuthenticationServiceImpl authenticationService;

    public AuthenticationController(AuthenticationServiceImpl authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody CreateUserRequest request ) {
        JsonObject response = (JsonObject) authenticationService.registerUser(request);
        if(response.has("username")) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }

    }
    @PreAuthorize("hasAuthority('SCOPE_message_read')")
    @PostMapping("/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello");
    }
    @PostMapping("/register-client/{clientName}")
    public ResponseEntity<?> registerClient(@PathVariable String clientName) {
        JsonObject response = (JsonObject) authenticationService.registerClient(clientName);
        if(response.has("clientId")) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }

    }

}
