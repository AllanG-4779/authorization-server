package com.allang.authorizationserver.service;

import com.allang.authorizationserver.dto.CreateUserRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

  Object registerClient( CreateUserRequest request);
}
