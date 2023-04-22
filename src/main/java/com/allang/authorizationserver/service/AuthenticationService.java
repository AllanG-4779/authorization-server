package com.allang.authorizationserver.service;

import com.allang.authorizationserver.dto.CreateUserRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
  Object registerUser(CreateUserRequest request);
  Object registerClient(String username);
}
