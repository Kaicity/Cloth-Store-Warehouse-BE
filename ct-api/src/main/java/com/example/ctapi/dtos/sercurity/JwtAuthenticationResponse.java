 package com.example.ctapi.dtos.sercurity;

import lombok.Data;

import java.time.LocalDateTime;

 @Data
public class JwtAuthenticationResponse {
    private String token;
    private String refreshToken;
    private LocalDateTime timeStart;
    private LocalDateTime timeEnd;
}
