package com.allang.authorizationserver.exceptions;

import com.allang.authorizationserver.dto.UniversalResponse;
import com.allang.authorizationserver.exceptions.exception.ParameterExists;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionHandler {
@org.springframework.web.bind.annotation.ExceptionHandler(ParameterExists.class)
    public ResponseEntity<?> handleDuplicateEntry(ParameterExists ex){
    UniversalResponse response = UniversalResponse.builder()
            .request_time(LocalDateTime.now())
            .status("01")
            .metadata("No metadata")
            .message(ex.getMessage())
            .build();
        return ResponseEntity.status(409).body(response);
    }
}
