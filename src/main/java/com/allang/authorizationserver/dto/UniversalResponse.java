package com.allang.authorizationserver.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UniversalResponse {
    @JsonFormat(timezone = "Africa/Nairobi", pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime request_time;
    private String status;
    private String message;
    private Object data;
    private Object metadata;


}
